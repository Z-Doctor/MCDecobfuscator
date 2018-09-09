package zdoctor.mcdeobfuscator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import zdoctor.commons.utils.Util;

public class Interface {

	private JFrame frmZdoctorDeobfuscator;
	private JTextField txtSearchField;
	protected int lastX;
	private JTextField console;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
					Interface window = new Interface();
					window.frmZdoctorDeobfuscator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
//		SwingUtilities.invokeLater(Manager::initialize);
		Manager.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmZdoctorDeobfuscator = new JFrame();
		frmZdoctorDeobfuscator.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frmZdoctorDeobfuscator.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmZdoctorDeobfuscator.setTitle("ZDoctor - Deobfuscator " + Constants.VERSION);
		frmZdoctorDeobfuscator.setBounds(100, 100, 600, 400);
		frmZdoctorDeobfuscator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmZdoctorDeobfuscator.getContentPane().setLayout(springLayout);

		JLabel lblFile = new JLabel("File:");
		frmZdoctorDeobfuscator.getContentPane().add(lblFile);

		JLabel lblMapping = new JLabel("Mapping:");
		springLayout.putConstraint(SpringLayout.WEST, lblMapping, 20, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		lblMapping.setFont(new Font("Tahoma", Font.BOLD, 11));
		frmZdoctorDeobfuscator.getContentPane().add(lblMapping);

		JLabel lblOutput = new JLabel("Output:");
		springLayout.putConstraint(SpringLayout.WEST, lblOutput, 0, SpringLayout.WEST, lblFile);
		frmZdoctorDeobfuscator.getContentPane().add(lblOutput);

		JLabel lblMappings = new JLabel("Mappings:");
		frmZdoctorDeobfuscator.getContentPane().add(lblMappings);

		JLabel lblSearch = new JLabel("Search:");
		springLayout.putConstraint(SpringLayout.WEST, lblSearch, 10, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		lblSearch.setFont(new Font("SansSerif", Font.BOLD, 12));
		frmZdoctorDeobfuscator.getContentPane().add(lblSearch);

		JLabel varInput = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, varInput, 0, SpringLayout.NORTH, lblFile);
		springLayout.putConstraint(SpringLayout.WEST, varInput, 6, SpringLayout.EAST, lblFile);
		springLayout.putConstraint(SpringLayout.SOUTH, varInput, 0, SpringLayout.SOUTH, lblFile);
		springLayout.putConstraint(SpringLayout.EAST, varInput, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(varInput);

		JLabel varOutput = new JLabel("");
		springLayout.putConstraint(SpringLayout.WEST, varOutput, 6, SpringLayout.EAST, lblOutput);
		springLayout.putConstraint(SpringLayout.SOUTH, varOutput, 0, SpringLayout.SOUTH, lblOutput);
		springLayout.putConstraint(SpringLayout.EAST, varOutput, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(varOutput);

		JLabel varMappings = new JLabel("0");
		springLayout.putConstraint(SpringLayout.EAST, lblMappings, -6, SpringLayout.WEST, varMappings);
		varMappings.setHorizontalAlignment(SwingConstants.RIGHT);
		springLayout.putConstraint(SpringLayout.NORTH, varMappings, 0, SpringLayout.NORTH, lblMappings);
		springLayout.putConstraint(SpringLayout.EAST, varMappings, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(varMappings);

		JButton btnOpen = new JButton("Open");
		springLayout.putConstraint(SpringLayout.NORTH, lblFile, 6, SpringLayout.NORTH, btnOpen);
		btnOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Manager.open();
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, lblFile, 6, SpringLayout.EAST, btnOpen);
		springLayout.putConstraint(SpringLayout.NORTH, btnOpen, 10, SpringLayout.NORTH,
				frmZdoctorDeobfuscator.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnOpen, 10, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(btnOpen);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("Fetches the latest mappings list");
		springLayout.putConstraint(SpringLayout.NORTH, lblMappings, 6, SpringLayout.NORTH, btnRefresh);
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Manager.reloadMapping();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnRefresh, -4, SpringLayout.NORTH, lblMapping);
		frmZdoctorDeobfuscator.getContentPane().add(btnRefresh);

		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Manager.save();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, lblOutput, 6, SpringLayout.NORTH, btnSave);
		springLayout.putConstraint(SpringLayout.NORTH, lblMapping, 8, SpringLayout.SOUTH, btnSave);
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, 0, SpringLayout.SOUTH, btnOpen);
		springLayout.putConstraint(SpringLayout.NORTH, varOutput, 4, SpringLayout.NORTH, btnSave);
		springLayout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, btnOpen);
		springLayout.putConstraint(SpringLayout.EAST, btnSave, 0, SpringLayout.EAST, btnOpen);
		frmZdoctorDeobfuscator.getContentPane().add(btnSave);

		JButton btnStart = new JButton("Start");
		springLayout.putConstraint(SpringLayout.WEST, btnStart, 9, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (btnStart.isEnabled())
					Manager.start();
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnStart, -5, SpringLayout.SOUTH,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(btnStart);

		JComboBox<Constants.MapType> mapType = new JComboBox<>();
		mapType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;

				if (e.getItem() == Constants.MapType.Custom) {
					Manager.hideMiscMapping();
					Manager.loadCustomMapping();
				} else if (e.getItem() == Constants.MapType.Live) {
					Manager.hideMiscMapping();
					Util.runThread(Manager::downloadOrLoadMapping);
				} else {
					Manager.setMappingVisible(true);
					Util.runThread(Manager::loadMappings);
				}

				if (e.getItem() == Constants.MapType.Obf) {
//					Manager.hideMiscMapping2();
//					Util.runThread(Manager::loadMappings);
					Manager.disableStart(true);
				} else
					Manager.disableStart(false);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, mapType, -3, SpringLayout.NORTH, lblMapping);
		springLayout.putConstraint(SpringLayout.WEST, mapType, 6, SpringLayout.EAST, lblMapping);
		mapType.setModel(new DefaultComboBoxModel<Constants.MapType>(Constants.MapType.values()));
		frmZdoctorDeobfuscator.getContentPane().add(mapType);

		JComboBox<String> mcVersion = new JComboBox<>();
		mcVersion.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED)
					return;
				Util.runThread(Manager::loadMappings);
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, mcVersion, 0, SpringLayout.EAST, mapType);
		springLayout.putConstraint(SpringLayout.NORTH, mcVersion, -3, SpringLayout.NORTH, lblMapping);
		frmZdoctorDeobfuscator.getContentPane().add(mcVersion);

		JComboBox<Long> mapVersion = new JComboBox<>();
		mapVersion.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					return;
				Util.runThread(Manager::downloadOrLoadMapping);
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnRefresh, 0, SpringLayout.EAST, mapVersion);
		springLayout.putConstraint(SpringLayout.NORTH, mapVersion, -3, SpringLayout.NORTH, lblMapping);
		springLayout.putConstraint(SpringLayout.WEST, mapVersion, 0, SpringLayout.EAST, mcVersion);
		frmZdoctorDeobfuscator.getContentPane().add(mapVersion);

		JProgressBar progressBar = new JProgressBar();
		springLayout.putConstraint(SpringLayout.WEST, progressBar, 113, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnStart, 0, SpringLayout.WEST, progressBar);
		progressBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lastX = e.getX();
			}
		});
		progressBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (progressBar.isIndeterminate()) {
					progressBar.setValue(progressBar.getValue() + (lastX - e.getX()));
					lastX = e.getX();
				}
			}
		});
		progressBar.setIndeterminate(true);
		springLayout.putConstraint(SpringLayout.NORTH, progressBar, 0, SpringLayout.NORTH, btnStart);
		springLayout.putConstraint(SpringLayout.SOUTH, progressBar, 0, SpringLayout.SOUTH, btnStart);
		springLayout.putConstraint(SpringLayout.EAST, progressBar, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(progressBar);

		txtSearchField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, txtSearchField, 6, SpringLayout.EAST, lblSearch);
		springLayout.putConstraint(SpringLayout.EAST, txtSearchField, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, lblSearch, 6, SpringLayout.NORTH, txtSearchField);
		txtSearchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_SHIFT)
					return;

				if (Character.isWhitespace(e.getKeyChar()))
					e.consume();
				if (!Character.isLetterOrDigit(e.getKeyChar())
						&& (e.getKeyChar() != '_' && e.getKeyChar() != '$' && e.getKeyChar() != '/'))
					e.consume();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
					try {
						String clipBoard = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
								.getData(DataFlavor.stringFlavor);
						StringSelection selection = new StringSelection(
								clipBoard.replaceAll(Constants.ILLEGAL_CHAR, ""));
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
					} catch (HeadlessException | UnsupportedFlavorException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Manager.search();
			}
		});
		txtSearchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Manager.searchFocus(true);
			}

			@Override
			public void focusLost(FocusEvent e) {
				Manager.searchFocus(false);
			}
		});
		txtSearchField.setText("Search Field");
		springLayout.putConstraint(SpringLayout.NORTH, txtSearchField, 6, SpringLayout.SOUTH, btnRefresh);
		frmZdoctorDeobfuscator.getContentPane().add(txtSearchField);
		txtSearchField.setColumns(10);

		JScrollPane scrollPane2 = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPane2, 10, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		scrollPane2.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane2, 6, SpringLayout.SOUTH, txtSearchField);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane2, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(scrollPane2);

		JList<String> resultList = new JList<>();
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				String selected = resultList.getSelectedValue();
				if (selected != null && !selected.equals(""))
					Manager.autoFill(selected);
			}
		});
		scrollPane2.setViewportView(resultList);

		console = new JTextField();
		console.setEnabled(false);
		console.setEditable(false);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane2, 0, SpringLayout.NORTH, console);
		springLayout.putConstraint(SpringLayout.WEST, console, 10, SpringLayout.WEST,
				frmZdoctorDeobfuscator.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, console, 0, SpringLayout.NORTH, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, console, -10, SpringLayout.EAST,
				frmZdoctorDeobfuscator.getContentPane());
		frmZdoctorDeobfuscator.getContentPane().add(console);
		console.setColumns(10);

		Manager.setInputLabel(varInput);
		Manager.setOutputLabel(varOutput);
		Manager.setMappingLabel(varMappings);

		Manager.setMappingCombo(btnRefresh, mapType, mcVersion, mapVersion);

		Manager.setSearchText(txtSearchField);
		Manager.setResultList(resultList);
		Manager.setConsole(console);

		Manager.setProgressBar(progressBar);
		Manager.setStartButton(btnStart);
	}
}
