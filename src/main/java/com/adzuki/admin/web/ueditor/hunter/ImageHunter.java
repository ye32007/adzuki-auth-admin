package com.adzuki.admin.web.ueditor.hunter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.adzuki.admin.web.ueditor.define.AppInfo;
import com.adzuki.admin.web.ueditor.define.BaseState;
import com.adzuki.admin.web.ueditor.define.MIMEType;
import com.adzuki.admin.web.ueditor.define.MultiState;
import com.adzuki.admin.web.ueditor.define.State;

/**
 * 图片抓取器
 * @author hancong03@baidu.com
 *
 */
public class ImageHunter {

//	private String filename = null;
//	private String savePath = null;
//	private String rootPath = null;
	private List<String> allowTypes = null;
	private long maxSize = -1;
	
	private List<String> filters = null;
	
	public ImageHunter ( Map<String, Object> conf ) {
		
//		this.filename = (String)conf.get( "filename" );
//		this.savePath = (String)conf.get( "savePath" );
//		this.rootPath = (String)conf.get( "rootPath" );
		this.maxSize = (Long)conf.get( "maxSize" );
		this.allowTypes = Arrays.asList( (String[])conf.get( "allowFiles" ) );
		this.filters = Arrays.asList( (String[])conf.get( "filter" ) );
		
	}
	
	public State capture (String[] list ) {
		
		MultiState state = new MultiState( true );
		
		for ( String source : list ) {
			state.addState( captureRemoteData( source ) );
		}
		
		return state;
		
	}

	public State captureRemoteData ( String urlStr ) {
		
		HttpURLConnection connection = null;
		URL url = null;
		String suffix = null;
		
		try {
			url = new URL( urlStr );

			if ( !validHost( url.getHost() ) ) {
				return new BaseState( false, AppInfo.PREVENT_HOST );
			}
			
			connection = (HttpURLConnection) url.openConnection();
		
			connection.setInstanceFollowRedirects( true );
			connection.setUseCaches( true );
		
			if ( !validContentState( connection.getResponseCode() ) ) {
				return new BaseState( false, AppInfo.CONNECTION_ERROR );
			}
			
			suffix = MIMEType.getSuffix( connection.getContentType() );
			
			if ( !validFileType( suffix ) ) {
				return new BaseState( false, AppInfo.NOT_ALLOW_FILE_TYPE );
			}
			
			if ( !validFileSize( connection.getContentLength() ) ) {
				return new BaseState( false, AppInfo.MAX_SIZE );
			}
			
//			String savePath = this.getPath( this.savePath, this.filename, suffix );
//			String physicalPath = this.rootPath + savePath;

//			State state = StorageManager.saveFileByInputStream( connection.getInputStream(), physicalPath );
			String originalFileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
			
			// 改造UEditor，将远程抓取的图片上传到文件服务器
			
			// 将远程文件输入流拷贝操作
			InputStream is = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];  
			int len;  
			while ((len = is.read(buffer)) > -1 ) {  
			    baos.write(buffer, 0, len);  
			}  
			baos.flush();  
			
			// 获取文件的byte序列
			byte[] bytes = baos.toByteArray();
			
			// TODO 上传文件到OSS

			// 生成文件key
//			String key = OssUtils.createKey(OssUtils.FILE_PATH_UEDITOR, suffix);
//			// 创建上传Object的Metadata
//			ObjectMetadata meta = new ObjectMetadata();
//			// TODO 设置文件缓存一个月，需要判断文件类型
//			meta.setCacheControl("max-age=2592000");
//			// 上传到oss
//			OssUtils.uploadNormalFile(new ByteArrayInputStream(baos.toByteArray()), key, meta);

			// 设置返回结果
			State state = new BaseState(true);
			state.putInfo( "size", connection.getInputStream().available() );
			state.putInfo( "title", "" );
//			state.putInfo( "url", OssUtils.getFileDownloadPath(key) );
			state.putInfo( "source", urlStr );
			
			return state;
			
		} catch ( Exception e ) {
			return new BaseState( false, AppInfo.REMOTE_FAIL );
		}
		
	}
	
//	private String getPath ( String savePath, String filename, String suffix  ) {
//		return PathFormat.parse( savePath + suffix, filename );
//		
//	}
	
	private boolean validHost ( String hostname ) {
		
		return !filters.contains( hostname );
		
	}
	
	private boolean validContentState ( int code ) {
		
		return HttpURLConnection.HTTP_OK == code;
		
	}
	
	private boolean validFileType ( String type ) {
		
		return this.allowTypes.contains( type );
		
	}
	
	private boolean validFileSize ( int size ) {
		return size < this.maxSize;
	}
	
}
