package org.cmcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.cmcc.service.ExcelExportService;
import org.cmcc.service.SftpService;
import org.cmcc.service.StorageService;
import org.cmcc.service.dto.EntitySftpSqlDto;
import org.cmcc.vo.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传
 */
@Controller
@Slf4j
public class FileController {

    private final StorageService storageService;

    @Reference(protocol = "dubbo")
    private SftpService sftpService;

    @Reference(protocol = "dubbo")
    private ExcelExportService excelExportService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = storageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName).toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadFileAndSubmit")
    @ResponseBody
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("tableName") String tableName) {
        String fileName = storageService.storeFile(file, System.currentTimeMillis() + "");
        ;
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName).toUriString();

        UploadFileResponse uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        String resMsg = submit(tableName, fileName);
        uploadFileResponse.setMsg(resMsg);
        return uploadFileResponse;
    }

    @PostMapping("/uploadMultipleFiles")
    @ResponseBody
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files).map(this::uploadFile).collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = storageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/uploadSftp")
    @ResponseBody
    public String submit(String tableName, String fileName) {
        EntitySftpSqlDto entitySftpSql = sftpService.getEntitySftpSql(tableName);
        String dir = entitySftpSql.getSftpDir();
        String username = entitySftpSql.getSftpUsername();
        String host = entitySftpSql.getSftpHost();
        String port = entitySftpSql.getSftpPort();
        String pwd = entitySftpSql.getSftpPwd();
        String sql = entitySftpSql.getSftpSql();
        excelExportService.excelExportCus(tableName, fileName, null);
        //上传到sftp文件服务器
        return excelExportService.upload(new String[]{"fill" + fileName}, dir, username, host, port, pwd);
    }
}
