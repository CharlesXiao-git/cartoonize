package com.cartoonize.configuration;

import com.cartoonize.model.entity.ImageEntity;
import com.cartoonize.repository.ImageJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class ScheduledConfig {
    @Autowired
    private ImageJpaRepository imageJpaRepository;

    /**
     *  The job will run on at 10:00 on the first day of every month.
     *  If the image was created more then 3 months, the job will set the image to 'expired'.
     */
    @Scheduled(cron = "0 10 0 1 * ?")
    public void expiredImagesJob(){
        log.info("Finding expired images job start ...");
        List<ImageEntity> imagesEntityList = imageJpaRepository.findAll();
        imagesEntityList.forEach( imageEntity ->{
            String createdDateStr = imageEntity.getCreatedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate createdDate = LocalDate.parse(createdDateStr, formatter);
            long diff = ChronoUnit.MONTHS.between(createdDate, LocalDate.now());
            if(diff > 3) {
                imageEntity.setExpired(true);
                imageJpaRepository.save(imageEntity);
                log.info("File: "+ imageEntity.getImageName() + " expired !");
            }
        } );
        log.info("Job end!");
    }
}
