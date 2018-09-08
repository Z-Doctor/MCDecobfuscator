package zdoctor.mcdeobfuscator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import zdoctor.commons.utils.DebugUtil;
import zdoctor.commons.utils.StopWatch;
import zdoctor.commons.utils.Util;
import zdoctor.commons.utils.data.BinaryReader;
import zdoctor.commons.utils.data.FileUtil;
import zdoctor.commons.utils.data.NodeDictionary;
import zdoctor.commons.utils.data.ZipUtil;
import zdoctor.mcdeobfuscator.Constants.MapType;
import zdoctor.mcdeobfuscator.modes.Deobfuscator;

public class Manager {

	// Input Managers
	protected static final FileNameExtensionFilter ZIPFilter = new FileNameExtensionFilter("Zip Files", "zip");
	protected static final FileNameExtensionFilter JARFilter = new FileNameExtensionFilter("Jar Files", "jar");

	private static final JFileChooser inputChooser = new JFileChooser(".");
	private static final JFileChooser outputChooser = new JFileChooser(".");
	private static final JFileChooser mapChooser = new JFileChooser(".");
	static {
		inputChooser.setFileFilter(JARFilter);
		outputChooser.setFileFilter(JARFilter);

		inputChooser.setFileFilter(ZIPFilter);
		outputChooser.setFileFilter(ZIPFilter);
		mapChooser.setFileFilter(ZIPFilter);
	}

	// Labels & Gui
	private static JLabel inputLabel;
	private static JLabel outputLabel;
	private static JLabel mappingLabel;
	private static HashMap<String, HashMap<MapType, ArrayList<Long>>> versions;
	private static JProgressBar progressBar;
	private static JComboBox<?>[] mappingCombo;
	private static JButton mappingRefreshButton;
	private static JTextField searchField;
	private static JList<String> resultField;

	// Variables
	private static boolean init = false;
	private static boolean running = false;
	private static int count;
	private static int fileCount;
	private static int progress;
	private static final ArrayList<ZipEntry> targetEntries = new ArrayList<>();
	private static final ArrayList<ZipEntry> miscEntries = new ArrayList<>();
	private static NodeDictionary<String> dic = new NodeDictionary<>();
	private static File mappingFile;
	private static ZipFile inputZip;
	private static ZipOutputStream zipOutputStream;
	private static JTextField console;
	private static StringBuilder log;
	private static StopWatch stopWatch;

	// Label & Gui Setters
	public static void setInputLabel(JLabel label) {
		inputLabel = label;
	}

	public static void setOutputLabel(JLabel label) {
		outputLabel = label;
	}

	public static void setMappingLabel(JLabel label) {
		mappingLabel = label;
	}

	public static void setProgressBar(JProgressBar jbar) {
		progressBar = jbar;
	}

	public static void setMappingCombo(JButton button, JComboBox<?>... jBox) {
		mappingRefreshButton = button;
		mappingCombo = jBox;
	}

	public static void setSearchText(JTextField textField) {
		searchField = textField;
	}

	public static void setResultList(JList<String> textField) {
		resultField = textField;
	}

	public static void setConsole(JTextField textField) {
		console = textField;
	}

	// Update Gui Interface
	public static void initialize() {
		if (init)
			return;
		init = true;
		VersionHandler.reload();
		versions = VersionHandler.MAPPINGS;
		searchField.setText(Constants.DEFAULT_SEARCH);

		dic = new NodeDictionary<>();
		reloadMapping();
	}

	@SuppressWarnings("unchecked")
	public static void reloadMapping() {
		JComboBox<MapType> mapTypeBox = (JComboBox<MapType>) mappingCombo[0];
		mapTypeBox.setModel(new DefaultComboBoxModel<>(Constants.MapType.values()));
		JComboBox<String> mcVersionBox = (JComboBox<String>) mappingCombo[1];
		mcVersionBox.setModel(new DefaultComboBoxModel<>(versions.keySet().toArray(new String[0])));
		loadMappings();
	}

	@SuppressWarnings("unchecked")
	public static void loadMappings() {
		JComboBox<MapType> mapTypeBox = (JComboBox<MapType>) mappingCombo[0];
		JComboBox<String> mcVersionBox = (JComboBox<String>) mappingCombo[1];
		JComboBox<Long> mappingsBox = (JComboBox<Long>) mappingCombo[2];

		MapType type = (Constants.MapType) mapTypeBox.getSelectedItem();
		String mcVersion = (String) mcVersionBox.getSelectedItem();
		mappingsBox.setModel(new DefaultComboBoxModel<>(versions.get(mcVersion).get(type).toArray(new Long[0])));

		downloadOrLoadMapping();
	}

	@SuppressWarnings("unchecked")
	public static void downloadOrLoadMapping() {
		JComboBox<MapType> mapTypeBox = (JComboBox<MapType>) mappingCombo[0];
		JComboBox<String> mcVersionBox = (JComboBox<String>) mappingCombo[1];
		JComboBox<Long> mappingsBox = (JComboBox<Long>) mappingCombo[2];

		MapType type = (Constants.MapType) mapTypeBox.getSelectedItem();
		if (type == MapType.Live) {
			mappingFile = MappingCacheHandler.getLiveMapping();
		} else {

			String mcVersion = (String) mcVersionBox.getSelectedItem();
			if (mappingsBox.getSelectedItem() == null)
				return;

			long mapping = (long) mappingsBox.getSelectedItem();

			switch (type) {
			case Daily:
				mappingFile = MappingCacheHandler.getDailyMapping(mcVersion, mapping);
				break;
			case Stable:
				mappingFile = MappingCacheHandler.getStableMapping(mcVersion, mapping);
				break;
			default:
				break;
			}
		}

		dic = new NodeDictionary<>();

		try {
			loadToDictionaryFromZip(mappingFile);
		} catch (IOException e) {
			e.printStackTrace();
			DebugUtil.notifyUser("Error Parsing Zip", e.getLocalizedMessage());
		}

		mappingLabel.setText(Integer.toString(dic.count()));
	}

	public static void reset() {
		count = 0;
		progress = 0;
		progressBar.setValue(0);
	}

	public static void updateProgress(int i) {
		progress += i;
		progressBar.setValue((int) ((float) progress / fileCount * 100));
	}

	public static void hideMiscMapping() {
		boolean skipFirst = false;
		for (JComboBox<?> jBox : mappingCombo) {
			if (skipFirst)
				jBox.setVisible(false);
			skipFirst = true;
		}
		mappingRefreshButton.setVisible(false);
	}

	public static void setMappingVisible(boolean visible) {
		for (JComboBox<?> jBox : mappingCombo) {
			jBox.setVisible(visible);
		}
		mappingRefreshButton.setVisible(visible);
	}

	public static void searchFocus(boolean focusGained) {
		if (focusGained) {
			if (searchField.getText().equalsIgnoreCase(Constants.DEFAULT_SEARCH))
				searchField.setText("");
		} else {
			if (searchField.getText().equals(""))
				searchField.setText(Constants.DEFAULT_SEARCH);
		}

	}

	public static void writeToConsole(String line, Object... args) {
		Util.runThread(() -> {
			console.setText(String.format(line, args));
		});
	}

	public static void log(String format, Object... args) {
		log.append(String.format(format, args));
		log.append('\n');
	}

	public static void logReplacement(String key, String value) {
		count++;
		log("Replaced '%s' with '%s'", key, value);
	}

	// Settings
	public static void open() {
		if (inputChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			reset();
			parseInput();
		}
	}

	public static void save() {
		if (outputChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

			File temp = FileUtil.enforceFileExt(outputChooser.getSelectedFile(),
					((FileNameExtensionFilter) outputChooser.getFileFilter()));

			outputChooser.setSelectedFile(temp);

			outputLabel.setText(outputChooser.getSelectedFile().getName());
			outputLabel.setToolTipText(outputChooser.getSelectedFile().getPath());
		}
	}

	public static void search() {
		resultField.setListData(new String[0]);
		if (searchField.getText().length() > 0) {
			Object[] matches = dic.findMatches(searchField.getText().getBytes());
			String[] list = new String[matches.length];
			for (int i = 0; i < matches.length; i++) {
				list[i] = (String) matches[i];
			}
			resultField.setListData(list);
		}

	}

	public static void find(String text) {
		if (dic.hasKey(text))
			searchField.setText(dic.lookUp(text));
		else if (dic.hasValue(text))
			searchField.setText(new String(dic.getKey(text)));
		search();
	}

	public static void prepOutput() {
		try {
			if (zipOutputStream != null)
				zipOutputStream.close();

			if (outputChooser.getSelectedFile().getName().endsWith(".jar"))
				zipOutputStream = new JarOutputStream(new FileOutputStream(outputChooser.getSelectedFile()));
			else
				zipOutputStream = new ZipOutputStream(new FileOutputStream(outputChooser.getSelectedFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadCustomMapping() {
		if (mapChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				loadToDictionaryFromZip(mappingFile);
			} catch (IOException e) {
				e.printStackTrace();
				mappingCombo[0].setSelectedIndex(0);
				mapChooser.setSelectedFile(null);
			}
		} else {
			mappingCombo[0].setSelectedIndex(0);
			mapChooser.setSelectedFile(null);
		}
	}

	public static void parseInput() {
		targetEntries.clear();
		miscEntries.clear();

		try {
			inputZip = new ZipFile(inputChooser.getSelectedFile());
			inputLabel.setText(inputChooser.getSelectedFile().getName());
			inputLabel.setToolTipText(inputChooser.getSelectedFile().getPath());

			Enumeration<? extends ZipEntry> entries = inputZip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".java") || entry.getName().endsWith(".class"))
					targetEntries.add(entry);
				else
					miscEntries.add(entry);
			}

			fileCount = miscEntries.size() + targetEntries.size();
		} catch (IOException e) {
			e.printStackTrace();
			reset();
		}

	}

	public static void loadToDictionaryFromZip(File file) throws ZipException, IOException {
		ZipFile zip = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			loadToDictionary(ZipUtil.extractEntry(zip, entry));
		}
		zip.close();
	}

	public static void loadToDictionary(byte[] data) throws IOException {
		BinaryReader br = new BinaryReader(data);

		while (br.available() > 0) {
			String line = br.readLine();
			if (line.equalsIgnoreCase("searge,name,side,desc"))
				continue;
			String[] deobs = line.split(",");
			dic.register(deobs[0], deobs[1]);
		}
		br.close();
	}

	public static void loadToDictionary(File file) throws IOException {
		if (file == null)
			return;

		BinaryReader br = new BinaryReader(file);

		while (br.available() > 0) {
			String line = br.readLine();
			if (line.equalsIgnoreCase("searge,name,side,desc"))
				continue;
			String[] deobs = line.split(",");
			dic.register(deobs[0], deobs[1]);
		}

		br.close();
	}

	public static void start() {
		if (inputChooser.getSelectedFile() == null || outputChooser.getSelectedFile() == null)
			return;

		if (mappingFile == null || !mappingFile.exists())
			return;

		if (running)
			return;
		running = true;

		reset();
		prepOutput();
		writeToConsole("");
		log = new StringBuilder();
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		stopWatch = new StopWatch();

		Util.runThread(Manager::deobfuscate);
	}

	protected static void finished() {
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(false);
		log("");

		log(Constants.LOG, fileCount, count, stopWatch.getMillis() / 1000f);
		writeToConsole(Constants.LOG, fileCount, count, stopWatch.getMillis() / 1000f);
		System.out.println(String.format(Constants.LOG, fileCount, count, stopWatch.getMillis() / 1000f));
		try {
			zipOutputStream.close();
			zipOutputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		running = false;

	}

	// Modes
	public static void deobfuscate() {
		stopWatch.start();
		miscEntries.forEach(entry -> {
			byte[] data = ZipUtil.extractEntry(inputZip, entry);
			ZipUtil.writeToZip(zipOutputStream, entry.getName(), data);
			updateProgress(1);
		});
		targetEntries.forEach(entry -> {
			byte[] data = ZipUtil.extractEntry(inputZip, entry);
			try {
				data = Deobfuscator.deobfuscate(dic, data);
				ZipUtil.writeToZip(zipOutputStream, entry.getName(), data);
			} catch (IOException e) {
				e.printStackTrace();
			}

			updateProgress(1);
		});
		stopWatch.stop();
		finished();
	}

}
