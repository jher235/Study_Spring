package org.zerock.b01.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                //경로 uploadPath에 파일명을 uuid+"_"+originalName로 지정하는 경로
                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

                boolean image = false;

                try{
                    multipartFile.transferTo(savePath); //transferTo() 메소드는 주어진 Path에 파일을 저장함.

                    //이미지일 경우
                    if(Files.probeContentType(savePath).startsWith("image")){

                        image = true;

                        File thumbFile = new File(uploadPath, "s_"+uuid+"_"+originalName);

//                         200x200 픽셀 크기의 썸네일로 변환
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200,200);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                        .img(image)
                        .uuid(uuid)
                        .fileName(originalName)
                        .build()
                );

            });//end each

            return list;
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
