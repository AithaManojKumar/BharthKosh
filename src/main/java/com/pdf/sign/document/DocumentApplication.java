package com.pdf.sign.document;

import com.pdf.sign.document.util.FilePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;

@SpringBootApplication
public class DocumentApplication extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DocumentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DocumentApplication.class, args);
		/*System.out.println("Application is starting ");
		System.out.println(System.getProperty("user.dir"));
		
		logger.info("user Directory : "+System.getProperty("user.dir"));
		createUploadDirectory();*/
	}
	/*private static void createUploadDirectory(){
		try{
			File file = new File(FilePath.uploadDirectory);
			
			if (!file.exists()) {
				file.mkdirs();
			}

		}catch (Exception exception){
			exception.printStackTrace();
		}
	}*/
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DocumentApplication.class);
    }
}
