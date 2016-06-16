package org.jenkinsci.plugins.octoperf.log;

import com.google.common.io.Closer;
import hudson.FilePath;
import okhttp3.ResponseBody;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.octoperf.client.RestApiFactory;
import retrofit2.Call;

import java.io.*;
import java.util.Set;

final class JMeterLogService implements LogService {
  private static final String LOG_EXT = ".log";
  private static final String LOGS_FOLDER = "logs";
  
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
    logsFolder.mkdirs();
    int i = 0;
    for(final String filename : files) {
      final String outputFilename = "jmeter-" + i + LOG_EXT;
      final FilePath logFile = new FilePath(logsFolder, outputFilename);
    
      logger.println("Downloading log file: " + filename);
      final Call<ResponseBody> response = api.getFile(benchResultId, filename);
      final ResponseBody body = response.execute().body();
      
      final Closer closer = Closer.create();
      InputStream input = closer.register(new BufferedInputStream(body.byteStream()));
      try {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        input = factory.createCompressorInputStream(body.byteStream());
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
      i++;
    }
  }

}
