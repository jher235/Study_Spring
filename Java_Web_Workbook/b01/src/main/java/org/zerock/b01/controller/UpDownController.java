package org.zerock.b01.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.util.List;

@RestController
@Log4j2
public class UpDownController {

    @Value("${org.zerock.upload.path}")//import시에 springframewokr로 시작하는 value
    private String uploadPath;

//    @Operation(summary = "Upload POST", description = "POST방식으로 파일 등록")   //swagger 버전업으로 post방식 파일 업로드 안씀
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String upload(UploadFileDTO uploadFileDTO){
//        log.info(uploadFileDTO);
//
//        return null;
//    }

    @Operation(summary = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(@Parameter(
                        description = "Files to be uploaded",
                        content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
                    )
            UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles()!=null){
            uploadFileDTO.getFiles().forEach(multipartFile -> {
                log.info(multipartFile.getOriginalFilename());
            });//end each
        }//end if

        return null;
    }



    @Operation(summary = "Upload GET", description = "GET방식으로 파일 등록")
    @GetMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(MultipartFile multipartFile){

        log.info("multipartFile.......");


        return null;
    }


}
