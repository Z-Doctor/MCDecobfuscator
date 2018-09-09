package zdoctor.mcdeobfuscator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import zdoctor.commons.utils.WebDownloader;
import zdoctor.commons.utils.data.FileUtil;

public class MappingCacheHandler {

	public static File getDailyMapping(String mcVersion, long mapping) {
		File map = new File(Constants.CACHE_FOLDER, VersionHandler.getDailyZip(mcVersion, mapping));
		if (map.exists())
			return map;

		try {
			byte[] data = WebDownloader.downloadToArray(VersionHandler.getDailyURL(mcVersion, mapping));
			if (data.length <= 0)
				return null;
			FileUtil.flushToFile(map, data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File getStableMapping(String mcVersion, long mapping) {
		File map = new File(Constants.CACHE_FOLDER, VersionHandler.getStableZip(mcVersion, mapping));
		if (map.exists())
			return map;

		try {
			byte[] data = WebDownloader.downloadToArray(VersionHandler.getStableURL(mcVersion, mapping));
			if (data.length <= 0)
				return null;
			FileUtil.flushToFile(map, data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File getLiveMapping() {
		File liveZip = new File(Constants.CACHE_FOLDER, Constants.LIVE_ZIP);
		try {
//			ZipOutputStream zos = new ZipOutputStream(ne)
			byte[] fieldsData = WebDownloader.downloadToArray(Constants.LIVE_FIELDS);
			byte[] methodsData = WebDownloader.downloadToArray(Constants.LIVE_METHODS);
			byte[] paramsData = WebDownloader.downloadToArray(Constants.LIVE_PARAMS);

			if (fieldsData.length > 0 && methodsData.length > 0 && paramsData.length > 0) {
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(liveZip));
				zos.putNextEntry(new ZipEntry("fields.csv"));
				zos.write(fieldsData);
				zos.putNextEntry(new ZipEntry("methods.csv"));
				zos.write(methodsData);
				zos.putNextEntry(new ZipEntry("params.csv"));
				zos.write(paramsData);
				zos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (liveZip.exists())
			return liveZip;
		return null;
	}

	public static File getSRGMapping(String mcVersion) {
		File map = new File(Constants.CACHE_FOLDER, VersionHandler.getSRGZip(mcVersion));
		if (map.exists())
			return map;

		try {
			byte[] data = WebDownloader.downloadToArray(VersionHandler.getSRGURL(mcVersion));
			if (data.length <= 0)
				return null;
			FileUtil.flushToFile(map, data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			Manager.writeToConsole("SRG file for MC Version '%s' does not exist", mcVersion);
		}
		return null;
	}

}
