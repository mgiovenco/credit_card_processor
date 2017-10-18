package com.cardprocessor;

import com.cardprocessor.domain.CreditCardFileProcessingReport;
import com.cardprocessor.service.TextFileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

import static java.lang.System.exit;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private TextFileProcessingService processingService;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            CreditCardFileProcessingReport report = processingService.processFile(args[0]);

            System.out.println("############");
            report.outputTransactionSummaryOutput();
        } else {
            // Check if user is redirecting to this file.  If so, redirect to file and send path to service.
            if (System.in != null) {
                File file = new File(UUID.randomUUID() + ".txt");
                Files.copy(System.in, file.toPath());
                CreditCardFileProcessingReport report = processingService.processFile(file.getPath());

                System.out.println("############");
                report.outputTransactionSummaryOutput();
            }
        }

        exit(0);
    }
}