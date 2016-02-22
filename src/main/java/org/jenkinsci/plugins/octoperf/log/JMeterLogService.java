package org.jenkinsci.plugins.octoperf.log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;

import com.google.common.io.Closer;

import hudson.FilePath;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

final class JMeterLogService implements LogService {
  private static final String LOG_EXT = ".log";
  private static final String LOGS_FOLDER = "logs";
  
  @Override
  public void downloadLogFiles(
      final FilePath workspace,
      final PrintStream logger,
      final RestAdapter adapter, 
      final String benchResultId) throws IOException, InterruptedException {
    final LogApi api = adapter.create(LogApi.class);
    
    final Set<String> files = api.getFiles(benchResultId);
    logger.println("Available log files: " + files);
    
    final FilePath logsFolder = new FilePath(workspace, LOGS_FOLDER);
    logsFolder.mkdirs();
    int i = 0;
    for(final String filename : files) {
      final String outputFilename = "jmeter-" + i + LOG_EXT;
      final FilePath logFile = new FilePath(logsFolder, outputFilename);
    
      logger.println("Downloading log file: " + filename);
      final Response response = api.getFile(benchResultId, filename);
      final TypedInput body = response.getBody();
      
      final Closer closer = Closer.create();
      InputStream input = closer.register(new BufferedInputStream(body.in()));
      try {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        input = factory.createCompressorInputStream(body.in());
      } catch (final CompressorException e) {
        // file is probably not compressed
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
