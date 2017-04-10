package org.jenkinsci.plugins.octoperf.log;

import com.google.common.io.Closer;
import hudson.FilePath;
import okhttp3.ResponseBody;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Call;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;

final class JMeterLogService implements LogService {
  private static final String LOG_EXT = ".log";
  private static final String JTL_EXT = ".jtl";
  private static final String LOGS_FOLDER = "logs";
  private static final String JTLS_FOLDER = "jtls";
  
  @Override
  public void downloadLogFiles(
      final FilePath workspace,
      final PrintStream logger,
      final RestApiFactory apiFactory,
      final String benchResultId) throws IOException, InterruptedException {
    final LogApi api = apiFactory.create(LogApi.class);

    final Set<String> files = api.getFiles(benchResultId).execute().body();
    logger.println("Available log files: " + files);
    
    final FilePath logsFolder = new FilePath(workspace, LOGS_FOLDER);
    logsFolder.deleteContents();
    logsFolder.mkdirs();

    final FilePath jtlsFolder = new FilePath(workspace, JTLS_FOLDER);
    jtlsFolder.deleteContents();
    jtlsFolder.mkdirs();

    int logs = 0;
    int jtls = 0;
    for(final String filename : files) {
      String outputFilename = filename.replace(".gz", "");
      if (filename.endsWith("-agent" + LOG_EXT)) {
        // Skip monitoring agent logs
        continue;
      }

      final FilePath logFile;
      if (outputFilename.endsWith(JTL_EXT)) {
        logFile = new FilePath(jtlsFolder, "jmeter-" + jtls + JTL_EXT);
        jtls++;
      } else if(outputFilename.endsWith(LOG_EXT)) {
        logFile = new FilePath(logsFolder, "jmeter-" + logs + LOG_EXT);
        logs++;
      } else {
        continue;
      }

      logger.println("Downloading file: " + filename);
      final Call<ResponseBody> response = api.getFile(benchResultId, filename);
      final ResponseBody body = response.execute().body();
      
      final Closer closer = Closer.create();
      InputStream input = closer.register(new BufferedInputStream(body.byteStream()));
      try {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        input = factory.createCompressorInputStream(input);
      } catch (final CompressorException e) {
        // file is probably not compressed
        e.printStackTrace(logger);
      }
      
      try {
        final OutputStream output = closer.register(logFile.write());
        IOUtils.copy(input, output);
      } finally {
        closer.close();
      }
      logger.println("Archived log file: " + logFile);
    }
  }

}
