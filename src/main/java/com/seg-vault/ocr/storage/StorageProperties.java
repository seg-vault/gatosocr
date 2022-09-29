package com.seg-vault.ocr.storage;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String tmp = "tmp";
        
        private String perm = "perm";

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String location) {
		this.tmp = location;
	}
        
        public String getPerm() {
		return perm;
	}

	public void setPerm(String location) {
		this.perm = location;
	}

}