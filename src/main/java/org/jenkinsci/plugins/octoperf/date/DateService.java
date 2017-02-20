package org.jenkinsci.plugins.octoperf.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import static org.joda.time.DateTimeZone.UTC;

public interface DateService {
  DateService DATES = new JodaDateService(UTC);

  DateTimeZone dateTimeZone();

  DateTime now();
}
