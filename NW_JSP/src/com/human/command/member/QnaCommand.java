package com.human.command.member;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.human.command.member.MemberCommand;
import com.human.dao.BoardDao;
import com.human.dto.FileDto;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class QnaCommand implements MemberCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
		String qtype; //문의 종류
		String productId ; //상품아이디
		String customerID ; //이름
		String qTitle ; //제목
		String qContent ; //내용
		//비밀글 냅두고
	
		String savePath = "tempUpload"; // 업로드될 폴더이름
		int uploadFileSizeLimit = 10 * 1024 * 1024; //현재 10메가 제한 자바스크립트로 10메가 이상은 제한 해줘야함.(제약조건)
		String encType = "UTF-8";
		
		ServletContext context = request.getServletContext();
		String uploadFilePath = context.getRealPath(savePath);
		try {
			
			MultipartRequest multi = new MultipartRequest(request,
					uploadFilePath, uploadFileSizeLimit, encType,
					new DefaultFileRenamePolicy());
			
			qtype = multi.getParameter("qtype"); //문의 종류
			productId = multi.getParameter("productId"); //상품명
			customerID = multi.getParameter("customerID"); //아이디
			qTitle = multi.getParameter("qTitle"); //제목
			qContent = multi.getParameter("qContent"); //내용
			
			
			System.out.println("qtype : "+qtype);
			System.out.println("productId : "+productId);
			System.out.println("cstomerName : "+customerID);
			System.out.println("qTitle : "+qTitle);
			System.out.println("qContent : "+qContent);
			
			//데이터집어넣기
			BoardDao dao = new BoardDao();
			
			Enumeration files = multi.getFileNames();
			
			ArrayList<FileDto> dtos = new ArrayList<FileDto>();
			
			while (files.hasMoreElements()) {
				
				FileDto dto = new FileDto();
				String file = (String) files.nextElement();
				String file_name = multi.getFilesystemName(file);
				// 중복된 파일을 업로드할 경우 파일명이 바뀐다.
				String ori_file_name = multi.getOriginalFileName(file);
				String bytes = Integer.toString(file.length());
				
				dto.setFileNewName(file_name);
				dto.setFileOrgName(ori_file_name);
				dto.setFileSize(bytes);
				
				dtos.add(dto);
				
				System.out.println("file : "+file);
				System.out.println("file_name : "+file_name);
				System.out.println("ori_file_name : "+ori_file_name);
				System.out.println("uploadFilePath : "+uploadFilePath);
				System.out.println("bytes : "+bytes);
				
			}
			
			dao.insertQna(qtype, productId, customerID, qTitle, qContent, dtos);
			
			
		} catch (Exception e) {
			System.out.print("예외 발생 : " + e);
		}
		
		
		
		
		
		
		
	}

}
