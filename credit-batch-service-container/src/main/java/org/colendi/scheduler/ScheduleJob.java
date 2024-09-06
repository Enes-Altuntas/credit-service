package org.colendi.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.ports.input.service.CreditApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleJob {

  private final CreditApplicationService creditApplicationService;

  //@Scheduled(cron = "0 0 */3 * * *")
  @Scheduled(cron = "0 * * * * *")
  public void updateDatabaseRecords() {
    log.info("Updating Database Records");
  }

}