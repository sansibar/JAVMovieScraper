package moviescraper.doctord.GUI;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JList;

import moviescraper.doctord.IconCache;
import moviescraper.doctord.Movie;
import moviescraper.doctord.SearchResult;
import moviescraper.doctord.Thumb;
import moviescraper.doctord.XbmcXmlMovieBean;
import moviescraper.doctord.SiteParsingProfile.ActionJavParsingProfile;
import moviescraper.doctord.SiteParsingProfile.CaribbeancomPremiumParsingProfile;
import moviescraper.doctord.SiteParsingProfile.Data18MovieParsingProfile;
import moviescraper.doctord.SiteParsingProfile.Data18WebContentParsingProfile;
import moviescraper.doctord.SiteParsingProfile.DmmParsingProfile;
import moviescraper.doctord.SiteParsingProfile.IAFDParsingProfile;
import moviescraper.doctord.SiteParsingProfile.JavZooParsingProfile;
import moviescraper.doctord.SiteParsingProfile.SquarePlusParsingProfile;
import moviescraper.doctord.SiteParsingProfile.JavLibraryParsingProfile;
import moviescraper.doctord.SiteParsingProfile.SiteParsingProfile;
import moviescraper.doctord.dataitem.Actor;
import moviescraper.doctord.dataitem.Director;
import moviescraper.doctord.dataitem.Genre;
import moviescraper.doctord.dataitem.ID;
import moviescraper.doctord.dataitem.MPAARating;
import moviescraper.doctord.dataitem.OriginalTitle;
import moviescraper.doctord.dataitem.Outline;
import moviescraper.doctord.dataitem.Plot;
import moviescraper.doctord.dataitem.Rating;
import moviescraper.doctord.dataitem.Runtime;
import moviescraper.doctord.dataitem.Set;
import moviescraper.doctord.dataitem.SortTitle;
import moviescraper.doctord.dataitem.Studio;
import moviescraper.doctord.dataitem.Tagline;
import moviescraper.doctord.dataitem.Title;
import moviescraper.doctord.dataitem.Top250;
import moviescraper.doctord.dataitem.Trailer;
import moviescraper.doctord.dataitem.Votes;
import moviescraper.doctord.dataitem.Year;
import moviescraper.doctord.model.Renamer;
import moviescraper.doctord.preferences.MoviescraperPreferences;

import javax.swing.JLabel;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;

import javax.swing.JButton;

import java.awt.SystemColor;

import javax.swing.UIManager;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.Action;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;

import java.awt.Component;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class GUIMain {

	//Objects Used to Keep Track of Program State
	private List<File> currentlySelectedNfoFileList;
	private List<File> currentlySelectedPosterFileList;
	private List<File> currentlySelectedFolderJpgFileList;
	private List<File> currentlySelectedFanartFileList;
	private List<File> currentlySelectedTrailerFileList;
	private List<File> currentlySelectedMovieFileList;
	private List<File> currentlySelectedActorsFolderList;
	private List<File> currentlySelectedExtraFanartFolderList;
	private File currentlySelectedDirectoryList;
	private File defaultHomeDirectory;
	private MoviescraperPreferences preferences;
	
	//scraped movies
	private Movie currentlySelectedMovieDMM;
	private Movie currentlySelectedMovieActionJav;
	private Movie currentlySelectedMovieSquarePlus;
	private Movie currentlySelectedMovieJavLibrary;
	private Movie currentlySelectedMovieJavZoo;
	private Movie currentlySelectedMovieCaribbeancomPremium;
	private Movie currentlySelectedMovieData18Movie;
	private List <Movie> movieToWriteToDiskList;

	//Gui Elements
	private JFrame frmMoviescraper;
	private final Action moveToNewFolder = new MoveToNewFolderAction();
	private DefaultListModel<File> listModelFiles;
	
	private JPanel fileListPanel;
	private FileDetailPanel fileDetailPanel;

	private JScrollPane fileListScrollPane;
	private JList<File> fileList;
	private JFileChooser chooser;
	
	private ArtWorkPanel artWorkPanel;
	
	//variables for fileList
	private static int CHAR_DELTA = 1000;
	private String m_key;
	private long m_time;
	
	//Menus
	JMenuBar menuBar;
	JMenu preferenceMenu;
	
	//Dimensions of various elements
	private static final int iconSizeX = 16;
	private static final int iconSizeY = 16;

	private final static boolean debugMessages = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					GUIMain window = new GUIMain();
					System.out.println("Gui Initialized");
					window.frmMoviescraper.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIMain() {
		initialize();
	}
	
	private void debugWriter(String message)
	{
		if(debugMessages)
			System.out.println(message);
	}
	

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		preferences = new MoviescraperPreferences();
		currentlySelectedNfoFileList = new ArrayList<File>();
		currentlySelectedMovieFileList = new ArrayList<File>();
		currentlySelectedPosterFileList = new ArrayList<File>();
		currentlySelectedFolderJpgFileList = new ArrayList<File>();
		currentlySelectedFanartFileList = new ArrayList<File>();
		currentlySelectedTrailerFileList = new ArrayList<File>();
		currentlySelectedActorsFolderList = new ArrayList<File>();
		currentlySelectedExtraFanartFolderList = new ArrayList<File>();
		movieToWriteToDiskList = new ArrayList<Movie>();
		frmMoviescraper = new JFrame();
		frmMoviescraper.setBackground(SystemColor.window);
		frmMoviescraper.setPreferredSize(new Dimension(1024, 768));
		frmMoviescraper.setTitle("JAVMovieScraper");
		frmMoviescraper.setBounds(100, 100, 1024, 768);
		frmMoviescraper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//initialize the icons used in the program
		URL programIconURL = frmMoviescraper.getClass().getResource("/res/AppIcon.png");
		URL saveButtonIconURL = frmMoviescraper.getClass().getResource("/res/SaveButtonIcon.png");
		URL data18IconURL = frmMoviescraper.getClass().getResource("/res/Data18Icon.png");
		URL japanIconURL = frmMoviescraper.getClass().getResource("/res/JapanIcon.png");
		URL openIconURL = frmMoviescraper.getClass().getResource("/res/OpenIcon.png");
		URL fileFolderIconURL = frmMoviescraper.getClass().getResource("/res/FileFolderIcon.png");
		URL upIconURL = frmMoviescraper.getClass().getResource("/res/UpIcon.png");
		URL browseIconURL = frmMoviescraper.getClass().getResource("/res/BrowseDirectoryIcon.png");
		
		//Used for icon in the title bar
		Image programIcon = null;
		try {
			programIcon = ImageIO.read(programIconURL);
			if(programIcon != null)
				frmMoviescraper.setIconImage(programIcon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//used in the write to file button
		ImageIcon saveIcon = initializeImageIcon(saveButtonIconURL);
		
		//used in the scrape data18 buttons
		ImageIcon data18Icon = initializeImageIcon(data18IconURL);
		
		//used for scraping japanese movies
		ImageIcon japanIcon = initializeImageIcon(japanIconURL);
		
		//open the file icon
		ImageIcon openIcon = initializeImageIcon(openIconURL);
		
		//move to new folder icon
		ImageIcon moveToFolderIcon = initializeImageIcon(fileFolderIconURL);
		
		//up one folder icon
		ImageIcon upIcon = initializeImageIcon(upIconURL);
		
		//browse directory icon
		ImageIcon browseDirectoryIcon = initializeImageIcon(browseIconURL);

		fileListPanel = new JPanel();
//		fileListPanel.setPreferredSize(new Dimension(200, 10));
		frmMoviescraper.getContentPane().add(fileListPanel, BorderLayout.WEST);

		defaultHomeDirectory = preferences.getLastUsedDirectory();
		currentlySelectedDirectoryList = defaultHomeDirectory;
		FileList fl = new FileList();

		listModelFiles = new DefaultListModel<File>();
		fileList = new JList<File>(listModelFiles);
		
		//add in a keyListener so that you can start typing letters in the list and it will take you to that item in the list
		//if you type the second letter within CHAR_DELTA amount of time that will count as the Nth letter of the search
		//instead of the first
		fileList.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				//do nothing until the key is released

			}

			@Override
			public void keyReleased(KeyEvent e) {
				char ch = e.getKeyChar();

				// ignore searches for non alpha-numeric characters
				if (!Character.isLetterOrDigit(ch)) {
					return;
				}

				// reset string if too much time has elapsed
				if (m_time + CHAR_DELTA < System.currentTimeMillis()) {
					m_key = "";
				}

				m_time = System.currentTimeMillis();
				m_key += Character.toLowerCase(ch);

				// Iterate through items in the list until a matching prefix is found.
				// This technique is fine for small lists, however, doing a linear
				// search over a very large list with additional string manipulation
				// (eg: toLowerCase) within the tight loop would be quite slow.
				// In that case, pre-processing the case-conversions, and storing the
				// strings in a more search-efficient data structure such as a Trie
				// or a Ternary Search Tree would lead to much faster find.
				for (int i = 0; i < fileList.getModel().getSize(); i++) {
					String str = fileList.getModel().getElementAt(i).getName()
							.toString().toLowerCase();
					if (str.startsWith(m_key)) {
						fileList.setSelectedIndex(i); // change selected item in list
						fileList.ensureIndexIsVisible(i); // change listbox
						// scroll-position
						break;
					}

				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				//do nothing until the key is released
			}
		});
		
		//add mouse listener for double click
		fileList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() >= 2){
					@SuppressWarnings("unchecked")
					JList<File> theList = (JList<File>) e.getSource();
					try {
						File doubleClickedFile  = theList.getSelectedValue();
						if(doubleClickedFile != null && doubleClickedFile.exists() && doubleClickedFile.isDirectory())
						{
							try{
								currentlySelectedDirectoryList = doubleClickedFile;
								frmMoviescraper.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								updateFileListModel(currentlySelectedDirectoryList);
							}
							finally
							{
								preferences.setLastUsedDirectory(currentlySelectedDirectoryList);
								frmMoviescraper.setCursor(Cursor.getDefaultCursor());
							}
						}
						else
						{
							Desktop.getDesktop().open(theList.getSelectedValue());
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		fileList.addListSelectionListener(new SelectFileListAction());
		fileListScrollPane = fl.getGui(
				showFileListSorted(currentlySelectedDirectoryList), listModelFiles,
				true);
		fileListPanel.setLayout(new BoxLayout(fileListPanel, BoxLayout.Y_AXIS));
		fileListPanel.add(fileListScrollPane);
		fileListPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//set up buttons in the file panel
		
		JPanel fileListPanelButtonsPanel = new JPanel();
		fileListPanelButtonsPanel.setLayout( new BoxLayout(fileListPanelButtonsPanel, BoxLayout.Y_AXIS));
		fileListPanelButtonsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		fileListPanelButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
//		fileListPanelButtonsPanel.setMaximumSize(new Dimension(200,200));
		
		//Button to go up a directory for the current directory
		JButton btnUpDirectory = new JButton();
		btnUpDirectory.addActionListener(new UpDirectoryAction());
		btnUpDirectory.setIcon(upIcon);
		btnUpDirectory.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Button to bring up a file chooser so the user can browse and pick what directory they want to view
		JButton btnBrowseDirectory = new JButton("Browse Directory");
		btnBrowseDirectory.addActionListener(new BrowseDirectoryAction());
		btnBrowseDirectory.setIcon(browseDirectoryIcon);
		btnBrowseDirectory.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		fileListPanelButtonsPanel.add(btnUpDirectory);
		fileListPanelButtonsPanel.add(btnBrowseDirectory);
		fileListPanel.add(fileListPanelButtonsPanel);


		fileDetailPanel = new FileDetailPanel(preferences);
		JScrollPane FileDetailsScrollPane = new JScrollPane(fileDetailPanel);
		frmMoviescraper.getContentPane().add(FileDetailsScrollPane,
				BorderLayout.CENTER);
		
		artWorkPanel = fileDetailPanel.getArtWorkPanel();
		
		JPanel southPanel = new JPanel();
		southPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		frmMoviescraper.getContentPane().add(southPanel, BorderLayout.SOUTH);

		JComponent parserPanel = new SpecificParserPanel(this);
		parserPanel.setPreferredSize(new Dimension(200,50));
		southPanel.add(parserPanel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
		JPanel scrapeButtons = new JPanel();
		JPanel fileOperationsButtons = new JPanel();
		southPanel.add(buttonsPanel);

		JButton btnScrapeSelectMovieJAV = new JButton("Scrape JAV");
		btnScrapeSelectMovieJAV.setAction(new ScrapeMovieAction());
		btnScrapeSelectMovieJAV.setIcon(japanIcon);
		scrapeButtons.add(btnScrapeSelectMovieJAV);
		
		JButton btnScrapeSelectMovieJAVAutomatic = new JButton("Scrape JAV (Automatic)");
		btnScrapeSelectMovieJAVAutomatic.setAction(new ScrapeMovieActionAutomatic());
		btnScrapeSelectMovieJAVAutomatic.setIcon(japanIcon);
		scrapeButtons.add(btnScrapeSelectMovieJAVAutomatic);
		
		JButton btnScrapeSelectMovieData18Movie = new JButton("Scrape Data18 Movie");
		btnScrapeSelectMovieData18Movie.setAction(new ScrapeMovieActionData18Movie());
		if(data18Icon != null)
			btnScrapeSelectMovieData18Movie.setIcon(data18Icon);
		scrapeButtons.add(btnScrapeSelectMovieData18Movie);
		
		JButton btnScrapeSelectMovieData18WebContent = new JButton("Scrape Data18 Web Content");
		btnScrapeSelectMovieData18WebContent.setAction(new ScrapeMovieActionData18WebContent());
		if(data18Icon != null)
			btnScrapeSelectMovieData18WebContent.setIcon(data18Icon);
		scrapeButtons.add(btnScrapeSelectMovieData18WebContent);
		
		JButton btnWriteFileData = new JButton("Write File Data");
		if(saveIcon != null)
			btnWriteFileData.setIcon(saveIcon);
		btnWriteFileData.addActionListener(new WriteFileDataAction());
		fileOperationsButtons.add(btnWriteFileData);

		JButton btnMoveFileToFolder = new JButton("Move file to folder");
		btnMoveFileToFolder.setAction(moveToNewFolder);
		btnMoveFileToFolder.setIcon(moveToFolderIcon);
		fileOperationsButtons.add(btnMoveFileToFolder);

		JButton openCurrentlySelectedFileButton = new JButton(
				"Open File");
		openCurrentlySelectedFileButton.addActionListener(new OpenFileAction());
		openCurrentlySelectedFileButton.setIcon(openIcon);
		fileOperationsButtons.add(openCurrentlySelectedFileButton);
		
		buttonsPanel.add(scrapeButtons);
		buttonsPanel.add(fileOperationsButtons);
		
		initializeMenus();
	}
	
	private ImageIcon initializeImageIcon(URL url){
		try {
			BufferedImage iconBufferedImage = ImageIO.read(url);
			iconBufferedImage = Scalr.resize(iconBufferedImage, Method.QUALITY, iconSizeX, iconSizeY, Scalr.OP_ANTIALIAS);
			return new ImageIcon(iconBufferedImage);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	private void initializeMenus(){
		menuBar = new JMenuBar();
		
		//Set up the preferences menu
		preferenceMenu = new JMenu("Preferences");
		preferenceMenu.setMnemonic(KeyEvent.VK_P);
		preferenceMenu.getAccessibleContext().setAccessibleDescription(
				"Preferences for JAVMovieScraper");


		//Checkbox for writing fanart and poster
		JCheckBoxMenuItem writeFanartAndPosters = new JCheckBoxMenuItem("Write fanart and poster files");
		writeFanartAndPosters.setState(preferences.getWriteFanartAndPostersPreference());
		writeFanartAndPosters.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setWriteFanartAndPostersPreference(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setWriteFanartAndPostersPreference(false);

			}
		});
		preferenceMenu.add(writeFanartAndPosters);

		//Checkbox for overwriting fanart and poster
		JCheckBoxMenuItem overwriteFanartAndPosters = new JCheckBoxMenuItem("Overwrite fanart and poster files");
		overwriteFanartAndPosters.setState(preferences.getOverWriteFanartAndPostersPreference());
		overwriteFanartAndPosters.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setOverWriteFanartAndPostersPreference(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setOverWriteFanartAndPostersPreference(false);

			}
		});
		preferenceMenu.add(overwriteFanartAndPosters);

		//Checkbox for overwriting writing actors to .actor folder
		JCheckBoxMenuItem writeActorImages = new JCheckBoxMenuItem("Write Actor Images");
		writeActorImages.setState(preferences.getDownloadActorImagesToActorFolderPreference());
		writeActorImages.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setDownloadActorImagesToActorFolderPreference(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setDownloadActorImagesToActorFolderPreference(false);

			}
		});
		preferenceMenu.add(writeActorImages);

		//Checkbox for scraping extrafanart		
		JCheckBoxMenuItem scrapeExtraFanart = new JCheckBoxMenuItem("Scrape and Download Extrafanart (Only Works if Directory Selected When Scraping)");
		scrapeExtraFanart.setState(preferences.getExtraFanartScrapingEnabledPreference());
		scrapeExtraFanart.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setExtraFanartScrapingEnabledPreference(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setExtraFanartScrapingEnabledPreference(false);

			}
		});
		preferenceMenu.add(scrapeExtraFanart);

		//Checkbox for also creating folder.jpg	in addition to the poster file jpg
		JCheckBoxMenuItem createFolderJpg = new JCheckBoxMenuItem("Create folder.jpg for each folder");
		createFolderJpg.setState(preferences.getCreateFolderJpgEnabledPreference());
		createFolderJpg.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setCreateFolderJpgEnabledPreference(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setCreateFolderJpgEnabledPreference(false);

			}
		});
		preferenceMenu.add(createFolderJpg);
		
		//Checkbox for using fanart.jpg and poster.jpg, not moviename-fanart.jpg and moviename-poster.jpg
		JCheckBoxMenuItem noMovieNameInImageFiles = new JCheckBoxMenuItem("Save poster and fanart as fanart.jpg and poster.jpg instead of moviename-fanart.jpg and moviename-poster.jpg");
		noMovieNameInImageFiles.setState(preferences.getNoMovieNameInImageFiles());
		noMovieNameInImageFiles.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setNoMovieNameInImageFiles(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setNoMovieNameInImageFiles(false);

			}
		});
		preferenceMenu.add(noMovieNameInImageFiles);
		
		//Checkbox for writing the trailer to file
		JCheckBoxMenuItem writeTrailerToFile = new JCheckBoxMenuItem("Write Trailer To File");
		writeTrailerToFile.setState(preferences.getWriteTrailerToFile());
		writeTrailerToFile.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setWriteTrailerToFile(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setWriteTrailerToFile(false);

			}
		});
		preferenceMenu.add(writeTrailerToFile);
		
		//Checkbox for naming .nfo file movie.nfo instead of using movie name in file
		JCheckBoxMenuItem nfoNamedMovieDotNfo = new JCheckBoxMenuItem(".nfo file named movie.nfo instead of using movie name");
		nfoNamedMovieDotNfo.setState(preferences.getNfoNamedMovieDotNfo());
		nfoNamedMovieDotNfo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setNfoNamedMovieDotNfo(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setNfoNamedMovieDotNfo(false);

			}
		});
		preferenceMenu.add(nfoNamedMovieDotNfo);
		
		//Checkbox for using IAFD Actors instead of Data18
		JCheckBoxMenuItem useIAFDForActors = new JCheckBoxMenuItem("Using IAFD Actors instead of Data18");
		useIAFDForActors.setState(preferences.getUseIAFDForActors());
		useIAFDForActors.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setUseIAFDForActors(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setUseIAFDForActors(false);

			}
		});
		preferenceMenu.add(useIAFDForActors);
		
		//Checkbox for renaming Movie file
		JCheckBoxMenuItem renameMovieFile = new JCheckBoxMenuItem("Rename Movie File");
		renameMovieFile.setState(preferences.getRenameMovieFile());
		renameMovieFile.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				//save the menu choice off to the preference object (and the disk based settings file)
				if(e.getStateChange() == ItemEvent.SELECTED)
					preferences.setRenameMovieFile(true);
				else if(e.getStateChange() == ItemEvent.DESELECTED)
					preferences.setRenameMovieFile(false);

			}
		});
		preferenceMenu.add(renameMovieFile);
		
		JMenu renameMenu = new JMenu("Rename Settings");
		
		JMenuItem renameSettings = new JMenuItem("Rename Settings");
		renameSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RenamerGUI(preferences);
			}
		});
		renameMenu.add(renameSettings);
		
		//add the various menus together
		menuBar.add(preferenceMenu);
		menuBar.add(renameMenu);
		frmMoviescraper.setJMenuBar(menuBar);
	}
	protected void removeOldScrapedMovieReferences() {
		currentlySelectedMovieDMM = null;
		currentlySelectedMovieActionJav = null;
		currentlySelectedMovieSquarePlus = null;
		currentlySelectedMovieJavLibrary = null;
		currentlySelectedMovieJavZoo = null;
		currentlySelectedMovieCaribbeancomPremium = null;
		currentlySelectedMovieData18Movie = null;
		if(movieToWriteToDiskList != null)
			movieToWriteToDiskList.clear();

	}
	protected void removeOldSelectedFileReferences(){
		currentlySelectedNfoFileList.clear();
		currentlySelectedMovieFileList.clear();
		currentlySelectedActorsFolderList.clear();
		currentlySelectedPosterFileList.clear();
		currentlySelectedFolderJpgFileList.clear();
		currentlySelectedFanartFileList.clear();
		currentlySelectedTrailerFileList.clear();
		currentlySelectedExtraFanartFolderList.clear();
	}

	private void updateFileListModel(File currentlySelectedDirectory) {
		File [] filesToList = showFileListSorted(currentlySelectedDirectory);
		List<File> selectValuesListBeforeUpdate = fileList.getSelectedValuesList();
		listModelFiles.removeAllElements();
		for (File file : filesToList) {
			listModelFiles.addElement(file);
		}
		removeOldScrapedMovieReferences();
		removeOldSelectedFileReferences();
		
		//select the old values we had before we updated the list
		for(File currentValueToSelect : selectValuesListBeforeUpdate)
		{
			fileList.setSelectedValue(currentValueToSelect, false);
		}
	}

	private File[] showFileListSorted(File currentlySelectedDirectory) {

		File[] sortedList = currentlySelectedDirectory.listFiles();
		//Make a comparator so we get alphabetic order, with all directories first, then all the files (Like Windows Explorer)
		Comparator<File> comp = new Comparator<File>() {
			public int compare(File file1, File file2) {
				// Directory before non-directory
				if (file1.isDirectory() && !file2.isDirectory()) {

					return -1;
				}
				// Non-directory after directory
				else if (!file1.isDirectory() && file2.isDirectory()) {

					return 1;
				}
				// Alphabetic order otherwise
				else {

					return file1.compareTo(file2);
				}
			}
		};
		Arrays.sort(sortedList, comp);
		return sortedList;
	}
	
	//try to fill in any holes in thumbnails from the sourceMovie by looking through movieToGetExtraInfoFrom and see if has them
	//TODO: debug this
	private ArrayList<Actor> amalgamateActor(Movie sourceMovie, Movie movieToGetExtraInfoFrom)
	{
		ArrayList<Actor> amalgamatedActorList = new ArrayList<Actor>();
		boolean changeMade = false;
		if(sourceMovie.getActors() != null && movieToGetExtraInfoFrom.getActors() != null)
		{
			for(Actor currentActor : sourceMovie.getActors())
			{
				if(currentActor.getThumb() == null || currentActor.getThumb().getThumbURL().getPath().length() < 1)
				{
					//Found an actor with no thumbnail in sourceMovie
					for(Actor extraMovieActor: movieToGetExtraInfoFrom.getActors())
					{
						//scan through other movie and find actor with same name as the one we are currently on
						if(currentActor.getName().equals(extraMovieActor.getName()) && (extraMovieActor.getThumb() != null) && extraMovieActor.getThumb().getThumbURL().getPath().length() > 1)
						{
							currentActor = extraMovieActor;
							changeMade = true;
						}
					}
				}
				amalgamatedActorList.add(currentActor);
			}
		}
		if(changeMade)
		{
			return amalgamatedActorList;
		}
		else return sourceMovie.getActors(); // we didn't find any changes needed so just return the source movie's actor list
	}
	// Look through the fields in the various scraped movies and try to
	// automatically guess what the best data is and construct a Movie based on
	// that
	protected Movie amalgamateMovie(Movie currentlySelectedMovieDMM,
			Movie currentlySelectedMovieActionJav,
			Movie currentlySelectedMovieSquarePlus,
			Movie currentlySelectedMovieJavLibrary,
			Movie currentlySelectedMovieJavZoo,
			int movieNumberInList) {

		if (currentlySelectedMovieDMM == null
				&& currentlySelectedMovieActionJav == null
				&& currentlySelectedMovieSquarePlus == null
				&& currentlySelectedMovieJavLibrary == null)
			return null;
		// the case when i'm reading in a movie from a nfo file
		else if (movieToWriteToDiskList != null
				&& currentlySelectedMovieDMM == null
				&& currentlySelectedMovieActionJav == null
				&& currentlySelectedMovieSquarePlus == null
				&& currentlySelectedMovieJavLibrary == null) {
			return movieToWriteToDiskList.get(movieNumberInList);
		} else if (currentlySelectedMovieJavLibrary != null
				&& currentlySelectedMovieDMM != null
				&& (currentlySelectedMovieActionJav != null || currentlySelectedMovieSquarePlus != null || currentlySelectedMovieJavZoo != null)) {
			currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieDMM
					.getPlot());
			currentlySelectedMovieJavLibrary
					.setOriginalTitle(currentlySelectedMovieDMM
							.getOriginalTitle());
			currentlySelectedMovieJavLibrary.setSet(currentlySelectedMovieDMM.getSet());
			// grabbing the things from ActionJav which tend to be high quality
			// info
			if (currentlySelectedMovieActionJav != null
					&& currentlySelectedMovieActionJav.getPlot() != null
					&& currentlySelectedMovieActionJav.getPlot().getPlot()
							.length() > 1 && currentlySelectedMovieActionJav.getId().getId().equals(currentlySelectedMovieJavLibrary.getId().getId()))
				currentlySelectedMovieJavLibrary
						.setPlot(currentlySelectedMovieActionJav.getPlot());
			/*if (currentlySelectedMovieSquarePlus != null
					&& currentlySelectedMovieSquarePlus.getTitle() != null
					&& currentlySelectedMovieSquarePlus.getTitle().getTitle()
							.length() > 1)
				currentlySelectedMovieJavLibrary
						.setTitle(currentlySelectedMovieSquarePlus.getTitle());*/
			/*if (currentlySelectedMovieActionJav != null
					&& currentlySelectedMovieActionJav.getTitle() != null
					&& currentlySelectedMovieActionJav.getTitle().getTitle()
							.length() > 1)
				currentlySelectedMovieJavLibrary
						.setTitle(currentlySelectedMovieActionJav.getTitle());*/
			if (currentlySelectedMovieJavLibrary.getActors().size() == 0
					&& currentlySelectedMovieActionJav != null
					&& currentlySelectedMovieActionJav.getActors().size() > 0)
				currentlySelectedMovieJavLibrary
						.setActors(currentlySelectedMovieActionJav.getActors());
			currentlySelectedMovieJavLibrary.setFanart(currentlySelectedMovieDMM.getFanart());
			currentlySelectedMovieJavLibrary.setPosters(currentlySelectedMovieDMM.getPosters());
			if(currentlySelectedMovieJavZoo != null && currentlySelectedMovieJavZoo.getSet() != null && currentlySelectedMovieJavZoo.getSet().getSet().length() > 0)
				currentlySelectedMovieJavLibrary.setSet(currentlySelectedMovieJavZoo.getSet());
			currentlySelectedMovieJavLibrary.setExtraFanart(currentlySelectedMovieDMM.getExtraFanart());
			if(currentlySelectedMovieActionJav != null && currentlySelectedMovieActionJav.getPlot() != null && currentlySelectedMovieActionJav.getPlot().getPlot().length() > 1  &&
					currentlySelectedMovieActionJav.getId().getId().equals(currentlySelectedMovieJavLibrary.getId().getId()))
				currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieActionJav.getPlot());
			currentlySelectedMovieJavLibrary.setTrailer(currentlySelectedMovieDMM.getTrailer());
			return currentlySelectedMovieJavLibrary;
		}

		else if (currentlySelectedMovieJavLibrary != null
				&& currentlySelectedMovieDMM != null
				&& currentlySelectedMovieActionJav != null) {
			currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieDMM
					.getPlot());
			currentlySelectedMovieJavLibrary
					.setOriginalTitle(currentlySelectedMovieDMM
							.getOriginalTitle());
			currentlySelectedMovieJavLibrary.setSet(currentlySelectedMovieDMM.getSet());
			// grabbing the things from ActionJav which tend to be high quality
			// info
			if (currentlySelectedMovieActionJav.getPlot() != null
					&& currentlySelectedMovieActionJav.getPlot().getPlot()
							.length() > 1 && currentlySelectedMovieActionJav.getId().getId().equals(currentlySelectedMovieJavLibrary.getId().getId()))
				currentlySelectedMovieJavLibrary
						.setPlot(currentlySelectedMovieActionJav.getPlot());
			/*if (currentlySelectedMovieActionJav.getTitle() != null
					&& currentlySelectedMovieActionJav.getTitle().getTitle()
							.length() > 1)
				currentlySelectedMovieJavLibrary
						.setTitle(currentlySelectedMovieActionJav.getTitle());*/
			if(currentlySelectedMovieJavZoo != null && currentlySelectedMovieJavZoo.getSet() != null && currentlySelectedMovieJavZoo.getSet().getSet().length() > 0)
				currentlySelectedMovieJavLibrary.setSet(currentlySelectedMovieJavZoo.getSet());
			currentlySelectedMovieJavLibrary.setExtraFanart(currentlySelectedMovieDMM.getExtraFanart());
			if(currentlySelectedMovieActionJav != null && currentlySelectedMovieActionJav.getPlot() != null && currentlySelectedMovieActionJav.getPlot().getPlot().length() > 1 && currentlySelectedMovieActionJav.getId().getId().equals(currentlySelectedMovieJavLibrary.getId().getId()))
				currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieActionJav.getPlot());
			currentlySelectedMovieJavLibrary.setTrailer(currentlySelectedMovieDMM.getTrailer());
			currentlySelectedMovieJavLibrary.setTrailer(currentlySelectedMovieDMM.getTrailer());
			return currentlySelectedMovieJavLibrary;
		}

		else if (currentlySelectedMovieJavLibrary != null
				&& currentlySelectedMovieDMM != null) {
			//System.out.println("Return Jav Lib movie with DMM Plot");
			currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieDMM
					.getPlot());
			currentlySelectedMovieJavLibrary
					.setOriginalTitle(currentlySelectedMovieDMM
							.getOriginalTitle());
			if(currentlySelectedMovieJavZoo != null && currentlySelectedMovieJavZoo.getSet() != null && currentlySelectedMovieJavZoo.getSet().getSet().length() > 0)
				currentlySelectedMovieJavLibrary.setSet(currentlySelectedMovieJavZoo.getSet());
			currentlySelectedMovieJavLibrary.setExtraFanart(currentlySelectedMovieDMM.getExtraFanart());
			if(currentlySelectedMovieActionJav != null && currentlySelectedMovieActionJav.getPlot() != null && currentlySelectedMovieActionJav.getPlot().getPlot().length() > 1 )
				currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieActionJav.getPlot());
			currentlySelectedMovieJavLibrary.setTrailer(currentlySelectedMovieDMM.getTrailer());
			return currentlySelectedMovieJavLibrary;
		}
		// DMM was not found but JavLibrary was? This shouldn't really happen
		// too often...
		else if (currentlySelectedMovieJavLibrary != null) {
			//System.out.println("Return Jav Lib movie");
			if(currentlySelectedMovieJavZoo != null && currentlySelectedMovieJavZoo.getSet() != null && currentlySelectedMovieJavZoo.getSet().getSet().length() > 0)
				currentlySelectedMovieJavLibrary.setSet(currentlySelectedMovieJavZoo.getSet());
			if(currentlySelectedMovieActionJav != null && currentlySelectedMovieActionJav.getPlot() != null && currentlySelectedMovieActionJav.getPlot().getPlot().length() > 1 )
				currentlySelectedMovieJavLibrary.setPlot(currentlySelectedMovieActionJav.getPlot());
			return currentlySelectedMovieJavLibrary;
		}

		// Nothing on either squareplus or actionjav or JavLibrary, so just
		// return the DMM info
		else if (currentlySelectedMovieActionJav == null
				&& currentlySelectedMovieSquarePlus == null) {
			// System.out.println("Adding in id number to title (DMM Only Case): "
			// + currentlySelectedMovieDMM.getId());
			// currentlySelectedMovieDMM.setTitle(new
			// Title(currentlySelectedMovieDMM.getTitle().getTitle() + " (" +
			// currentlySelectedMovieDMM.getId() + ")"));
			if(currentlySelectedMovieJavZoo != null && currentlySelectedMovieJavZoo.getSet() != null && currentlySelectedMovieJavZoo.getSet().getSet().length() > 0)
				currentlySelectedMovieDMM.setSet(currentlySelectedMovieJavZoo.getSet());
			
			return currentlySelectedMovieDMM;
		}
		// ActionJav found and SquarePlus not
		else if (currentlySelectedMovieActionJav != null
				&& currentlySelectedMovieSquarePlus == null) {
			//System.out.println("ActionJav found and SquarePlus not");
			ArrayList<Actor> actorsToUse = (currentlySelectedMovieActionJav
					.getActors().size() > 0 && currentlySelectedMovieActionJav
					.getActors().size() >= currentlySelectedMovieDMM
					.getActors().size()) ? currentlySelectedMovieActionJav
					.getActors() : currentlySelectedMovieDMM.getActors();
			ArrayList<Director> directorsToUse = (currentlySelectedMovieActionJav
					.getDirectors().size() > 0) ? currentlySelectedMovieActionJav
					.getDirectors() : currentlySelectedMovieDMM.getDirectors();
			Thumb[] fanartToUse = currentlySelectedMovieDMM.getFanart();
			Thumb[] extraFanartToUse = currentlySelectedMovieDMM.getExtraFanart();
			ArrayList<Genre> genresToUse = (currentlySelectedMovieActionJav
					.getGenres().size() > 1) ? currentlySelectedMovieActionJav
					.getGenres() : currentlySelectedMovieDMM.getGenres();
			ID idsToUse = currentlySelectedMovieDMM.getId();
			MPAARating mpaaToUse = currentlySelectedMovieDMM.getMpaa();
			OriginalTitle originalTitleToUse = currentlySelectedMovieDMM
					.getOriginalTitle();
			Outline outlineToUse = currentlySelectedMovieDMM.getOutline();
			Plot plotToUse = (currentlySelectedMovieActionJav.getPlot()
					.getPlot().length() > 1) ? currentlySelectedMovieActionJav
					.getPlot() : currentlySelectedMovieDMM.getPlot();
			Thumb[] postersToUse = currentlySelectedMovieDMM.getPosters();
			Year yearToUse = currentlySelectedMovieDMM.getYear();
			Votes votesToUse = currentlySelectedMovieDMM.getVotes();
			Top250 top250ToUse = currentlySelectedMovieDMM.getTop250();
			Title titleToUse = (currentlySelectedMovieActionJav.getTitle()
					.getTitle().length() > 1) ? currentlySelectedMovieActionJav
					.getTitle() : currentlySelectedMovieDMM.getTitle();
			Tagline taglineToUse = currentlySelectedMovieDMM.getTagline();
			Rating ratingToUse = currentlySelectedMovieDMM.getRating();
			Runtime runtimeToUse = (currentlySelectedMovieActionJav
					.getRuntime().getRuntime().length() > 1) ? currentlySelectedMovieActionJav
					.getRuntime() : currentlySelectedMovieDMM.getRuntime();
			Set setToUse = currentlySelectedMovieDMM.getSet();
			SortTitle sortTitleToUse = currentlySelectedMovieDMM.getSortTitle();
			Studio studioToUse = (currentlySelectedMovieActionJav.getStudio()
					.getStudio().length() > 1) ? currentlySelectedMovieActionJav
					.getStudio() : currentlySelectedMovieDMM.getStudio();
			Trailer trailerToUse = currentlySelectedMovieDMM.getTrailer();
			Movie amalgamatedMovie = new Movie(actorsToUse, directorsToUse,
					fanartToUse, extraFanartToUse, genresToUse, idsToUse, mpaaToUse,
					originalTitleToUse, outlineToUse, plotToUse, postersToUse,
					ratingToUse, runtimeToUse, setToUse, sortTitleToUse,
					studioToUse, taglineToUse, titleToUse, top250ToUse, trailerToUse,
					votesToUse, yearToUse);
			return amalgamatedMovie;
		}
		// Squareplus found something, actionjav did not
		else if (currentlySelectedMovieActionJav == null
				&& currentlySelectedMovieSquarePlus != null) {
			//System.out.println("Squareplus found something, actionjav did not");
			ArrayList<Actor> actorsToUse = currentlySelectedMovieDMM
					.getActors();
			ArrayList<Director> directorsToUse = currentlySelectedMovieDMM
					.getDirectors();
			Thumb[] fanartToUse = currentlySelectedMovieDMM.getFanart();
			Thumb[] extraFanartToUse = currentlySelectedMovieDMM.getExtraFanart();
			ArrayList<Genre> genresToUse = currentlySelectedMovieDMM
					.getGenres();
			ID idsToUse = currentlySelectedMovieDMM.getId();
			MPAARating mpaaToUse = currentlySelectedMovieDMM.getMpaa();
			OriginalTitle originalTitleToUse = currentlySelectedMovieDMM
					.getOriginalTitle();
			Outline outlineToUse = currentlySelectedMovieDMM.getOutline();
			Plot plotToUse = currentlySelectedMovieDMM.getPlot();
			Thumb[] postersToUse = currentlySelectedMovieDMM.getPosters();
			Year yearToUse = currentlySelectedMovieDMM.getYear();
			Votes votesToUse = currentlySelectedMovieDMM.getVotes();
			Top250 top250ToUse = currentlySelectedMovieDMM.getTop250();
			Trailer trailerToUse = currentlySelectedMovieDMM.getTrailer();
			Title titleToUse = currentlySelectedMovieSquarePlus.getTitle();
			Tagline taglineToUse = currentlySelectedMovieDMM.getTagline();
			Rating ratingToUse = currentlySelectedMovieDMM.getRating();
			Runtime runtimeToUse = currentlySelectedMovieDMM.getRuntime();
			Set setToUse = currentlySelectedMovieDMM.getSet();
			SortTitle sortTitleToUse = currentlySelectedMovieDMM.getSortTitle();
			Studio studioToUse = currentlySelectedMovieDMM.getStudio();
			Movie amalgamatedMovie = new Movie(actorsToUse, directorsToUse,
					fanartToUse, extraFanartToUse, genresToUse, idsToUse, mpaaToUse,
					originalTitleToUse, outlineToUse, plotToUse, postersToUse,
					ratingToUse, runtimeToUse, setToUse, sortTitleToUse,
					studioToUse, taglineToUse, titleToUse, top250ToUse, trailerToUse,
					votesToUse, yearToUse);
			return amalgamatedMovie;
		} else // amalgamate from all 3 sources
		{
			//System.out.println("amalgamate from all 3 sources");
			ArrayList<Actor> actorsToUse = (currentlySelectedMovieActionJav
					.getActors().size() > 0 && currentlySelectedMovieActionJav
					.getActors().size() >= currentlySelectedMovieDMM
					.getActors().size()) ? currentlySelectedMovieActionJav
					.getActors() : currentlySelectedMovieDMM.getActors();
			ArrayList<Director> directorsToUse = (currentlySelectedMovieActionJav
					.getDirectors().size() > 0) ? currentlySelectedMovieActionJav
					.getDirectors() : currentlySelectedMovieDMM.getDirectors();
			Thumb[] fanartToUse = currentlySelectedMovieDMM.getFanart();
			Thumb[] extraFanartToUse = currentlySelectedMovieDMM.getExtraFanart();
			ArrayList<Genre> genresToUse = (currentlySelectedMovieActionJav
					.getGenres().size() > 1) ? currentlySelectedMovieActionJav
					.getGenres() : currentlySelectedMovieDMM.getGenres();
			ID idsToUse = currentlySelectedMovieDMM.getId();
			MPAARating mpaaToUse = currentlySelectedMovieDMM.getMpaa();
			OriginalTitle originalTitleToUse = currentlySelectedMovieDMM
					.getOriginalTitle();
			Outline outlineToUse = currentlySelectedMovieDMM.getOutline();
			Plot plotToUse = (currentlySelectedMovieActionJav.getPlot()
					.getPlot().length() > 1 && currentlySelectedMovieActionJav.getId().getId().equals(currentlySelectedMovieDMM.getId().getId())) ? currentlySelectedMovieActionJav
					.getPlot() : currentlySelectedMovieDMM.getPlot();
			Thumb[] postersToUse = currentlySelectedMovieDMM.getPosters();
			Year yearToUse = currentlySelectedMovieDMM.getYear();
			Votes votesToUse = currentlySelectedMovieDMM.getVotes();
			Top250 top250ToUse = currentlySelectedMovieDMM.getTop250();
			Trailer trailerToUse = currentlySelectedMovieDMM.getTrailer();
			Title titleToUse = currentlySelectedMovieActionJav.getTitle();
			Tagline taglineToUse = currentlySelectedMovieDMM.getTagline();
			Rating ratingToUse = currentlySelectedMovieDMM.getRating();
			Runtime runtimeToUse = (currentlySelectedMovieActionJav
					.getRuntime().getRuntime().length() > 1) ? currentlySelectedMovieActionJav
					.getRuntime() : currentlySelectedMovieDMM.getRuntime();
			Set setToUse = currentlySelectedMovieDMM.getSet();
			SortTitle sortTitleToUse = currentlySelectedMovieDMM.getSortTitle();
			Studio studioToUse = (currentlySelectedMovieActionJav.getStudio()
					.getStudio().length() > 1) ? currentlySelectedMovieActionJav
					.getStudio() : currentlySelectedMovieDMM.getStudio();
			Movie amalgamatedMovie = new Movie(actorsToUse, directorsToUse,
					fanartToUse, extraFanartToUse, genresToUse, idsToUse, mpaaToUse,
					originalTitleToUse, outlineToUse, plotToUse, postersToUse,
					ratingToUse, runtimeToUse, setToUse, sortTitleToUse,
					studioToUse, taglineToUse, titleToUse, top250ToUse, trailerToUse,
					votesToUse, yearToUse);
			return amalgamatedMovie;
		}

	}

	protected void readMovieFromNfoFile(File nfoFile) {
		FileInputStream fisTargetFile = null;
		try {
			fisTargetFile = new FileInputStream(nfoFile);
			String targetFileStr = IOUtils.toString(fisTargetFile, "UTF-8");
			//Sometimes there's some junk before the prolog tag. Do a workaround to remove that junk.
			//This really isn't the cleanest way to do this, but it'll work for now
			//check first to make sure the string even contains <?xml so we don't loop through an invalid file needlessly
			if(targetFileStr.contains("<?xml"))
			{
				while(targetFileStr.length() > 0 && !targetFileStr.startsWith("<?xml"))
				{
					if(targetFileStr.length() > 1)
					{
						targetFileStr = targetFileStr.substring(1,targetFileStr.length());
					}
					else break;
				}
			}
			XbmcXmlMovieBean xmlMovieBean = XbmcXmlMovieBean.makeFromXML(targetFileStr);
			if(xmlMovieBean != null)
			{
				Movie movieFromNfo = xmlMovieBean.toMovie();
				movieToWriteToDiskList.add(movieFromNfo);
				if (currentlySelectedPosterFileList.get(0).exists()) {
					//we don't want to resize this poster later
					Thumb[] currentPosters = movieToWriteToDiskList.get(0).getPosters();
					Thumb fileFromDisk;
					if(currentPosters.length > 0 && currentPosters[0] != null && currentPosters[0].getThumbURL() != null)
						fileFromDisk = new Thumb(currentlySelectedPosterFileList.get(0), currentPosters[0].getThumbURL().toString());
					else
					{
						fileFromDisk = new Thumb(currentlySelectedPosterFileList.get(0));
						currentPosters = new Thumb[1];
					}
					currentPosters[0] = fileFromDisk;
				}

				// The poster read from the URL is not resized. We used to do a resize here when this was only a jav scraper, but for now i've turned this off
				else if (movieToWriteToDiskList.get(0).hasPoster()) {
					//Thumb[] currentPosters = movieToWriteToDiskList.get(0).getPosters();
					//this was the old method before I wrote in method from pythoncovercrop. it is no longer used
					/*currentPosters[0] = new Thumb(currentPosters[0].getThumbURL()
						.toString(), 52.7, 0, 0, 0);*/
					//for now don't resize
					//currentPosters[0] = new Thumb(currentPosters[0].getThumbURL().toString(), true);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);

		}
		finally
		{
			try {
				fisTargetFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				updateAllFieldsOfSite1Movie(false);
			}
		}
	}

	protected void clearAllFieldsOfSite1Movie() {
		fileDetailPanel.clearView();
		fileDetailPanel.setTitleEditable(false);
	}
	
	//Update the GUI so the user can see what is scraped in
	protected void updateAllFieldsOfSite1Movie(boolean forceUpdatePoster) {
		if (movieToWriteToDiskList == null || movieToWriteToDiskList.size() == 0) {
			clearAllFieldsOfSite1Movie();
		} else if (movieToWriteToDiskList != null && movieToWriteToDiskList.get(0) != null) {
			clearAllFieldsOfSite1Movie();
			
			//All the titles from the various versions scraped of this movie from the different sites
			if(movieToWriteToDiskList != null)
				fileDetailPanel.getCurrentMovie().getAllTitles().add( movieToWriteToDiskList.get(0).getTitle() );
			if(currentlySelectedMovieDMM != null)
				fileDetailPanel.getCurrentMovie().getAllTitles().add( currentlySelectedMovieDMM.getTitle() );
			if(currentlySelectedMovieJavLibrary != null)
				fileDetailPanel.getCurrentMovie().getAllTitles().add( currentlySelectedMovieJavLibrary.getTitle() );
			if(currentlySelectedMovieSquarePlus != null)
				fileDetailPanel.getCurrentMovie().getAllTitles().add( currentlySelectedMovieSquarePlus.getTitle() );
			if(currentlySelectedMovieActionJav != null)
				fileDetailPanel.getCurrentMovie().getAllTitles().add( currentlySelectedMovieActionJav.getTitle() );
			if(currentlySelectedMovieJavZoo != null)
				fileDetailPanel.getCurrentMovie().getAllTitles().add( currentlySelectedMovieJavZoo.getTitle() );
			if(fileDetailPanel.getCurrentMovie().getAllTitles().size() > 0)
				fileDetailPanel.setTitleEditable(true);
			
			//original title
			fileDetailPanel.getCurrentMovie().setOriginalTitle(movieToWriteToDiskList.get(0)
					.getOriginalTitle());
			//ID
			if(movieToWriteToDiskList.get(0).getId() != null)
				fileDetailPanel.getCurrentMovie().setId(movieToWriteToDiskList.get(0).getId());
			//Studio
			if(movieToWriteToDiskList.get(0).getStudio() != null)
				fileDetailPanel.getCurrentMovie().setStudio(movieToWriteToDiskList.get(0).getStudio());
			//Year
			if(movieToWriteToDiskList.get(0).getYear() != null)
				fileDetailPanel.getCurrentMovie().setYear(movieToWriteToDiskList.get(0).getYear());
			//Plot
			if(movieToWriteToDiskList.get(0).getPlot() != null)
				fileDetailPanel.getCurrentMovie().setPlot(movieToWriteToDiskList.get(0).getPlot());
			//Set
			if(movieToWriteToDiskList.get(0).getSet() != null)
				fileDetailPanel.getCurrentMovie().setSet(movieToWriteToDiskList.get(0).getSet());
			
			//Add in all the genres
			fileDetailPanel.getCurrentMovie().setGenres( movieToWriteToDiskList.get(0).getGenres() );

			//Actors
			fileDetailPanel.getCurrentMovie().setActors( movieToWriteToDiskList.get(0).getActors() );

			boolean posterFileUpdateOccured = false;
			boolean fanartFileUpdateOccured = false;
			if(!forceUpdatePoster)
			{
				// try to get the poster from a local file, if it exists
				//Maybe there is a file in the directory just called folder.jpg
				File potentialOtherPosterJpg = new File(Movie.getFileNameOfPoster(currentlySelectedMovieFileList.get(0), true));
				File potentialOtherFanartJpg = new File(Movie.getFileNameOfFanart(currentlySelectedMovieFileList.get(0), true));
				//the poster would be called moviename-poster.jpg
				File standardPosterJpg = new File(Movie.getFileNameOfPoster(currentlySelectedMovieFileList.get(0), false));
				File standardFanartJpg = new File(Movie.getFileNameOfFanart(currentlySelectedMovieFileList.get(0), false));
				if (currentlySelectedPosterFileList.get(0).exists()) {
					try {
						BufferedImage img = ImageIO.read(currentlySelectedPosterFileList.get(0));
						BufferedImage scaledImage = ArtWorkPanel.resizeToPoster(img);
						artWorkPanel.setNewPoster(scaledImage);
						posterFileUpdateOccured = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(currentlySelectedFanartFileList.get(0).exists())
				{
					try {
						//System.out.println("found the standard fanart");
						BufferedImage img = ImageIO.read(currentlySelectedFanartFileList.get(0));
						BufferedImage scaledImage = ArtWorkPanel.resizeToFanart(img);
						artWorkPanel.setNewFanart(scaledImage);
						fanartFileUpdateOccured = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//well we didn't find a poster file we were expecting, try to see if there is any file named poster.jpg in there
				if(currentlySelectedMovieFileList.get(0).isDirectory() && potentialOtherPosterJpg.exists() && !posterFileUpdateOccured)
				{
					try {
						//System.out.println("Reading in poster from other" + potentialOtherPosterJpg);
						ImageIcon newPosterIcon;
						BufferedImage img = ImageIO.read(potentialOtherPosterJpg);
						BufferedImage scaledImage = ArtWorkPanel.resizeToPoster(img);
						artWorkPanel.setNewPoster(scaledImage);
						posterFileUpdateOccured = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//well we didn't find a poster file we were expecting, try to see if there is any file named fanart.jpg in there
				if(currentlySelectedMovieFileList.get(0).isDirectory() && potentialOtherFanartJpg.exists() && !fanartFileUpdateOccured)
				{
					try {
						//System.out.println("Reading in fanart from other" + potentialOtherFanartJpg);
						ImageIcon newFanartIcon;
						BufferedImage img = ImageIO.read(potentialOtherFanartJpg);
						BufferedImage scaledImage = ArtWorkPanel.resizeToFanart(img);
						artWorkPanel.setNewFanart(scaledImage);
						fanartFileUpdateOccured = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				//just in case, also check to see if one called moviename-poster.jpg is there, even if we were expecting a poster.jpg due to the preference we set
				if(standardPosterJpg.exists() && !posterFileUpdateOccured)
				{
					try {
						//System.out.println("Reading in poster from moviename-poster" + potentialOtherPosterJpg);
						artWorkPanel.setNewPoster(new ImageIcon(
								standardPosterJpg.getCanonicalPath()));
						posterFileUpdateOccured = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//just in case, also check to see if one called moviename-fanart.jpg is there, even if we were expecting a poster.jpg due to the preference we set
				if(standardFanartJpg.exists() && !fanartFileUpdateOccured)
				{
					try {
						//System.out.println("Reading in fanart from moviename-fanart" + potentialOtherPosterJpg);
						ImageIcon newFanartIcon;
						BufferedImage img = ImageIO.read(standardFanartJpg);
						BufferedImage scaledImage = ArtWorkPanel.resizeToFanart(img);
						artWorkPanel.setNewFanart(scaledImage);
						/*lblFanartIcon.setIcon(new ImageIcon(
								standardFanartJpg.getCanonicalPath()));*/
						fanartFileUpdateOccured = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// otherwise read it from the URL specified by the object since we couldn't find any local file
			if (movieToWriteToDiskList.get(0).hasPoster() && !posterFileUpdateOccured) {
				try {
					Image posterImage = movieToWriteToDiskList.get(0).getPosters()[0]
							.getThumbImage();
					ImageIcon newPosterIcon = new ImageIcon(posterImage);
					BufferedImage img = (BufferedImage) newPosterIcon.getImage();
					BufferedImage scaledImage = ArtWorkPanel.resizeToPoster(img);
					artWorkPanel.setNewPoster(scaledImage);
					posterFileUpdateOccured = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
				}
			}
			
			//try to read the fanart from the url since we couldn't find any local file
			if (movieToWriteToDiskList.get(0).hasFanart() && !fanartFileUpdateOccured) {
				System.out.println("Reading in the fanart from the url");
				try {
					Image fanartImage = movieToWriteToDiskList.get(0).getFanart()[0]
							.getThumbImage();
					ImageIcon newFanartIcon = new ImageIcon(fanartImage);
					BufferedImage img = (BufferedImage) newFanartIcon.getImage();
					BufferedImage scaledImage = ArtWorkPanel.resizeToFanart(img);
					artWorkPanel.setNewFanart(scaledImage);
					fanartImage = scaledImage;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		fileDetailPanel.updateView();
	}



	private class OpenFileAction implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			for(int movieNumberInList = 0; movieNumberInList < currentlySelectedMovieFileList.size(); movieNumberInList++)
			{
				if (currentlySelectedMovieFileList != null) {
					try {
						Desktop.getDesktop().open(currentlySelectedMovieFileList.get(movieNumberInList));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		}
	}

	private class WriteFileDataAction implements ActionListener {


		public void actionPerformed(ActionEvent arg0) {
			for(int movieNumberInList = 0; movieNumberInList < currentlySelectedMovieFileList.size(); movieNumberInList++)
			{
				try {
					//Display a wait cursor since file IO sometimes takes a little bit of time
					frmMoviescraper.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// Write the user or automatic selection using amalgamation
					// of different scraping sites
					if(movieToWriteToDiskList == null)
					{
						Movie amalgamationAutoPickMovie = amalgamateMovie(
								currentlySelectedMovieDMM,
								currentlySelectedMovieActionJav,
								currentlySelectedMovieSquarePlus,
								currentlySelectedMovieJavLibrary,
								currentlySelectedMovieJavZoo, movieNumberInList);

						movieToWriteToDiskList.add(amalgamationAutoPickMovie);
					}
					if(movieToWriteToDiskList.get(movieNumberInList) == null)
					{
						System.out.println("No match for this movie in the array, skipping writing");
						continue;
					}
					System.out.println("Writing this movie to file: "
							+ movieToWriteToDiskList);
					if(movieToWriteToDiskList != null)
					{
						if ( preferences.getRenameMovieFile() ) {
							////TODO New Filewrite
							System.out.println(currentlySelectedMovieFileList.get(0));
							File oldMovieFile = currentlySelectedMovieFileList.get(movieNumberInList);
							Movie movie = movieToWriteToDiskList.get(movieNumberInList);
							
							String sanitizerString = preferences.getSanitizerForFilename();
							String renameString = preferences.getRenamerString(); 
							Renamer renamer = new Renamer(renameString, sanitizerString, movie, oldMovieFile);
							String newMovieFilename = renamer.getNewFileName();
							System.out.println( "New Filename : " + newMovieFilename );
							File newMovieFile = new File(newMovieFilename);
							oldMovieFile.renameTo(newMovieFile);
							
							movieToWriteToDiskList.get(movieNumberInList).writeToFile(
									new File( movie.getFileNameOfNfo(newMovieFile, preferences.getNfoNamedMovieDotNfo()) ),
									new File( movie.getFileNameOfPoster(newMovieFile, preferences.getNoMovieNameInImageFiles()) ),
									new File( movie.getFileNameOfFanart(newMovieFile, preferences.getNoMovieNameInImageFiles())),
									new File( movie.getFileNameOfFolderJpg(newMovieFile) ),
									preferences);							
						} else {
							//save without renaming movie
							movieToWriteToDiskList.get(movieNumberInList).writeToFile(
							currentlySelectedNfoFileList.get(movieNumberInList),
							currentlySelectedPosterFileList.get(movieNumberInList),
							currentlySelectedFanartFileList.get(movieNumberInList),
							currentlySelectedFolderJpgFileList.get(movieNumberInList),
							preferences);
						}

						//we can only output extra fanart if we're scraping a folder, because otherwise the extra fanart will get mixed in with other files
						if(preferences.getExtraFanartScrapingEnabledPreference() && currentlySelectedMovieFileList.get(movieNumberInList).isDirectory() && currentlySelectedExtraFanartFolderList != null)
						{
							writeExtraFanart(null, movieNumberInList);
						}
					}
					//now write out the actor images if the user preference is set
					if(preferences.getDownloadActorImagesToActorFolderPreference() && currentlySelectedMovieFileList != null && currentlySelectedDirectoryList != null)
					{
						updateActorsFolder();
						//Don't create an empty .actors folder with no actors underneath it
						if(movieToWriteToDiskList.get(movieNumberInList).hasAtLeastOneActorThumbnail() && currentlySelectedActorsFolderList != null)
						{
							//File actorsFolder = new File(currentlySelectedMovieFile.getPath() + "\\.actors");
							FileUtils.forceMkdir(currentlySelectedActorsFolderList.get(movieNumberInList));
							//on windows this new folder should have the hidden attribute; on unix it is already "hidden" by having a . in front of the name
							Path path = currentlySelectedActorsFolderList.get(movieNumberInList).toPath();
							Boolean hidden = (Boolean) Files.getAttribute(path, "dos:hidden", LinkOption.NOFOLLOW_LINKS);
							if (hidden != null && !hidden) {
								Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
							}

							for(Actor currentActor : movieToWriteToDiskList.get(movieNumberInList).getActors())
							{
								String currentActorToFileName = currentActor.getName().replace(' ', '_');
								File fileNameToWrite = new File(currentlySelectedActorsFolderList.get(movieNumberInList).getPath() + "\\" + currentActorToFileName + ".jpg");
								currentActor.writeImageToFile(fileNameToWrite);
							}

						}

					}

					//write out the trailer to disk, if the preference for it is enabled
					Trailer trailerToWrite = movieToWriteToDiskList.get(movieNumberInList).getTrailer();
					if(preferences.getWriteTrailerToFile() && trailerToWrite != null && trailerToWrite.getTrailer().length() > 0)
					{
						//don't rewrite a file if it already exists since a trailer requires downloading from the web. this is a slow operation!
						if(!currentlySelectedTrailerFileList.get(movieNumberInList).exists())
						{
							System.out.println("Starting write of " + trailerToWrite.getTrailer() + " into file " + currentlySelectedTrailerFileList.get(movieNumberInList));
							trailerToWrite.writeTrailerToFile(currentlySelectedTrailerFileList.get(movieNumberInList));
						}
					}
					/*
					//we're outputting new files to the current visible directory, so we'll want to update GUI with the fact that they are there
					if(!currentlySelectedMovieFileList.get(movieNumberInList).isDirectory())
					{
						//fireListSelectionEvents = false;
						int selectedIndex = fileList.getSelectedIndex();
						int itemsAdded = 1;
						if(!listModelFiles.contains(currentlySelectedNfoFileList.get(movieNumberInList)))
						{
							listModelFiles.add(selectedIndex + itemsAdded,
									currentlySelectedNfoFileList.get(movieNumberInList));
							itemsAdded++;
						}
						if(!listModelFiles.contains(currentlySelectedFanartFileList.get(movieNumberInList)) && preferences.getWriteFanartAndPostersPreference())
						{
							listModelFiles.add(selectedIndex + itemsAdded,
									currentlySelectedFanartFileList.get(movieNumberInList));
							itemsAdded++;
						}
						if(!listModelFiles.contains(currentlySelectedPosterFileList.get(movieNumberInList)) && preferences.getWriteFanartAndPostersPreference())
						{
							listModelFiles.add(selectedIndex + itemsAdded,
									currentlySelectedPosterFileList.get(movieNumberInList));
							itemsAdded++;
						}
						if(!listModelFiles.contains(currentlySelectedTrailerFileList.get(movieNumberInList)) && preferences.getWriteTrailerToFile())
						{
							listModelFiles.add(selectedIndex + itemsAdded,
									currentlySelectedTrailerFileList.get(movieNumberInList));
							itemsAdded++;
						}
						if(!listModelFiles.contains(currentlySelectedFolderJpgFileList.get(movieNumberInList)) && preferences.getCreateFolderJpgEnabledPreference())
						{
							listModelFiles.add(selectedIndex + itemsAdded, currentlySelectedFolderJpgFileList.get(movieNumberInList ));
							itemsAdded++;
						}
					}
					*/
					System.out.println("Finished writing a movie file");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					frmMoviescraper.setCursor(Cursor.getDefaultCursor());
					JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
				}
				finally{
					frmMoviescraper.setCursor(Cursor.getDefaultCursor());
				}
			}
			//out of loop and done writing files, update the gui
			updateFileListModel(currentlySelectedDirectoryList);
		}
	}

	private class BrowseDirectoryAction implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			chooser = new JFileChooser();
			//remember our last used directory and start the search there
			if(preferences.getLastUsedDirectory().exists())
				chooser.setCurrentDirectory(preferences.getLastUsedDirectory());
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Movies", "avi", "mp4", "wmv", "flv", "mov", "rm", "mkv");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(frmMoviescraper);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				currentlySelectedDirectoryList = chooser.getSelectedFile();
				
				//display a wait cursor while repopulating the list
				//as this can sometimes be slow
				try{
					frmMoviescraper.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					updateFileListModel(currentlySelectedDirectoryList);
				}
				finally
				{
					preferences.setLastUsedDirectory(currentlySelectedDirectoryList);
					frmMoviescraper.setCursor(Cursor.getDefaultCursor());
				}
				

			}
		}
	}

	private class SelectFileListAction implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {

				if (fileList.getSelectedIndex() == -1) {
					// No selection
					// Clear out old selection references
					removeOldSelectedFileReferences();

				} else {

					removeOldSelectedFileReferences();
					
					// Item is selected
					//File selectedValue = fileList.getSelectedValue();
					for(File currentSelectedFile : fileList.getSelectedValuesList())
					{
						currentlySelectedNfoFileList.add(new File(Movie
								.getFileNameOfNfo(currentSelectedFile, preferences.getNfoNamedMovieDotNfo())));
						currentlySelectedPosterFileList.add(new File(Movie
								.getFileNameOfPoster(currentSelectedFile, preferences.getNoMovieNameInImageFiles())));
						currentlySelectedFolderJpgFileList.add(new File(Movie
								.getFileNameOfFolderJpg(currentSelectedFile)));
						currentlySelectedFanartFileList.add(new File(Movie
								.getFileNameOfFanart(currentSelectedFile, preferences.getNoMovieNameInImageFiles())));
						currentlySelectedTrailerFileList.add(new File(Movie.getFileNameOfTrailer(currentSelectedFile)));
					}
					
					debugWriter("nfos after selection: " + currentlySelectedNfoFileList);
					debugWriter("posters after selection: " + currentlySelectedPosterFileList);
					debugWriter("folderjpgs after selection: " + currentlySelectedFolderJpgFileList);
					debugWriter("fanartfiles after selection: " + currentlySelectedFanartFileList);
					debugWriter("trailer after selection: " + currentlySelectedTrailerFileList);
					

					currentlySelectedMovieFileList = fileList.getSelectedValuesList();
					


					updateActorsFolder();
					updateExtraFanartFolder(null);
					
					// clean up old scraped movie results from previous selection
					removeOldScrapedMovieReferences();

					if (currentlySelectedNfoFileList.get(0).exists()) {
						readMovieFromNfoFile(currentlySelectedNfoFileList.get(0));
					}
					//if no nfo file for file or directory, we want to just update the gui
					else
					{
						updateAllFieldsOfSite1Movie(false);
					}
				}
			}
		}
	}

	private class MoveToNewFolderAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2250733525782269006L;

		public MoveToNewFolderAction() {
			putValue(NAME, "Move File to New Folder");
			putValue(SHORT_DESCRIPTION, "Move File to New Folder");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String pathSeperator = System.getProperty("file.separator");
			for(int movieNumberInList = 0; movieNumberInList < currentlySelectedMovieFileList.size(); movieNumberInList++)
			{
			try {
				//set the cursor to busy as this could take more than 1 or 2 seconds while files are copied or extrafanart is downloaded from the internet
				frmMoviescraper.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (currentlySelectedMovieFileList != null
						&& currentlySelectedMovieFileList.get(movieNumberInList).exists() && currentlySelectedMovieFileList.get(movieNumberInList).isFile()) {
					// we can append the movie title to resulting folder name if
					// the movie is scraped, has an ID and generally matches the
					// ID in the filename (assuming the file is only named the
					// ID of the movie)
					String destinationDirectoryPrefix = "";
					if (movieToWriteToDiskList != null && movieToWriteToDiskList.size() > 0) {
						String possibleID = movieToWriteToDiskList.get(movieNumberInList).getId().getId()
								.toUpperCase();
						String possibleIDWithoutDash = possibleID.replaceFirst(
								"-", "");
						String fileNameComparingTo = FilenameUtils
								.getBaseName(currentlySelectedMovieFileList.get(movieNumberInList)
										.getName().toUpperCase());
						if (possibleID.equals(SiteParsingProfile.stripDiscNumber(fileNameComparingTo))
								|| possibleIDWithoutDash
										.equals(SiteParsingProfile.stripDiscNumber(fileNameComparingTo))) {
							destinationDirectoryPrefix = movieToWriteToDiskList.get(movieNumberInList)
									.getTitle().getTitle() + " - ";
							// replace illegal characters in the movie filename
							// prefix that the OS doesn't allow with blank space
							destinationDirectoryPrefix = destinationDirectoryPrefix
									.replace("^\\.+", "").replaceAll(
											"[\\\\/:*?\"<>|]", "");
						}

					}
					File destDir = new File(
							currentlySelectedMovieFileList.get(movieNumberInList).getParentFile()
									.getCanonicalPath()
									+ pathSeperator
									+ destinationDirectoryPrefix
									+ SiteParsingProfile.stripDiscNumber(FilenameUtils
											.getBaseName(currentlySelectedMovieFileList.get(movieNumberInList)
													.getName())));
					clearAllFieldsOfSite1Movie();
					//copy over the .actor folder items to the destination folder, but only if the preference is set and the usual sanity checking is done
					if (currentlySelectedMovieFileList.get(movieNumberInList).isFile() && currentlySelectedActorsFolderList != null && preferences.getDownloadActorImagesToActorFolderPreference())
					{
						File [] actorFilesToCopy = actorFolderFiles(movieNumberInList);
						File actorsFolderDestDir = new File(destDir.getPath() + "\\.actors");
						for(File currentFile : actorFilesToCopy)
						{
							FileUtils.copyFileToDirectory(currentFile, actorsFolderDestDir);
						}
					}
					if (currentlySelectedMovieFileList.get(movieNumberInList).exists())
					{
						//In case of stacked movie files (Movies which are split into multiple files such AS CD1, CD2, etc) get the list of all files
						//which are part of this movie's stack
						File currentDirectory = currentlySelectedMovieFileList.get(movieNumberInList).getParentFile();
						String currentlySelectedMovieFileWihoutStackSuffix = SiteParsingProfile.stripDiscNumber(FilenameUtils.removeExtension(currentlySelectedMovieFileList.get(movieNumberInList).getName()));
						if(currentDirectory != null)
						{

							for(File currentFile : currentDirectory.listFiles())
							{
								String currentFileNameWithoutStackSuffix = SiteParsingProfile.stripDiscNumber(FilenameUtils.removeExtension(currentFile.getName()));
								if(currentFile.isFile() && currentFileNameWithoutStackSuffix.equals(currentlySelectedMovieFileWihoutStackSuffix))
								{
									//this should also get the nfo file as a nice side effect
									FileUtils.moveFileToDirectory(currentFile,destDir, true);
								}
							}
						}

					}
					if (currentlySelectedNfoFileList.get(movieNumberInList).exists())
						FileUtils.moveFileToDirectory(currentlySelectedNfoFileList.get(movieNumberInList),destDir, true);
					if (currentlySelectedPosterFileList.get(movieNumberInList).exists()) {
						//if we're going to create folder.jpg file, just grab the poster file we already have and make a copy of it in the new folder
						if(preferences.getCreateFolderJpgEnabledPreference())
						{
							File currentlySelectedFolderJpg = new File(Movie.getFileNameOfFolderJpg(destDir));
							FileUtils.copyFile(currentlySelectedPosterFileList.get(movieNumberInList), currentlySelectedFolderJpg );
						}
						FileUtils.moveFileToDirectory(currentlySelectedPosterFileList.get(movieNumberInList), destDir, true);
					}
					if (currentlySelectedFanartFileList.get(movieNumberInList).exists()) {
						FileUtils.moveFileToDirectory(currentlySelectedFanartFileList.get(movieNumberInList), destDir, true);
					}
					
					if(currentlySelectedTrailerFileList.get(movieNumberInList).exists())
					{
						FileUtils.moveFileToDirectory(currentlySelectedTrailerFileList.get(movieNumberInList), destDir, true);
					}
					
					//if we are supposed to write the extrafanart, make sure to write that too
					
					if(preferences.getExtraFanartScrapingEnabledPreference())
					{
						writeExtraFanart(destDir, movieNumberInList);
					}
					

					// remove all the old references so we aren't tempted to
					// reuse them
					removeOldSelectedFileReferences();
					removeOldScrapedMovieReferences();
					updateFileListModel(currentlySelectedDirectoryList);
				}
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				frmMoviescraper.setCursor(Cursor.getDefaultCursor());
			}
			}
		}

	}
	
	private class ScrapeMovieAction extends AbstractAction {
		/**
		 * 
		 */
		String overrideURLDMM;
		String overrideURLJavLibrary;
		String overrideURLData18Movie;
		String overrideURLIAFD;
		private static final long serialVersionUID = 1L;
		boolean promptUserForURLWhenScraping; //do we stop to ask the user to pick a URL when scraping
		boolean scrapeJAV = true;
		boolean scrapeData18Movie = false;
		boolean scrapeData18WebContent = false;
		boolean manuallyPickFanart = true;
		boolean manuallyPickPoster = true;
		
		public SearchResult showOptionPane(SearchResult [] searchResults, String siteName)
		{
			if(searchResults.length > 0)
			{

				SelectionDialog selectionDialog = new SelectionDialog(searchResults, siteName);

				int result = JOptionPane.showOptionDialog(null, selectionDialog, "Select Movie to Scrape From " + siteName,
		                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
		                null, null, null);
				return selectionDialog.getSelectedValue();
			}
			else return null;
		}
		

		public ScrapeMovieAction() {
			putValue(NAME, "Scrape JAV");
			putValue(SHORT_DESCRIPTION, "Scrape Selected Movie");
			overrideURLDMM = "";
			overrideURLJavLibrary = "";
			overrideURLData18Movie = "";
			promptUserForURLWhenScraping = true;
		}

		public void actionPerformed(ActionEvent e) {
			try
			{
				//this takes a while to do, so set the cursor to busy
				frmMoviescraper.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));	

				// clear out all old values of the scraped movie
				removeOldScrapedMovieReferences();
				clearOverrides();
				for(int movieNumberInList = 0; movieNumberInList < currentlySelectedMovieFileList.size(); movieNumberInList++)
				{
					//set the cursor to busy as this may take a while

					// We don't want to block the UI while waiting for a time consuming
					// scrape, so make new threads for each scraping query




					if(promptUserForURLWhenScraping && scrapeJAV)
					{
						//bring up some dialog boxes so the user can choose what URL to use for each site
						try {
							DmmParsingProfile dmmPP = new DmmParsingProfile();
							JavLibraryParsingProfile jlPP = new JavLibraryParsingProfile();
							String searchStringDMM = dmmPP.createSearchString(currentlySelectedMovieFileList.get(movieNumberInList));
							SearchResult [] searchResultsDMM = dmmPP.getSearchResults(searchStringDMM);
							String searchStringJL = jlPP.createSearchString(currentlySelectedMovieFileList.get(movieNumberInList));
							if(searchResultsDMM != null && searchResultsDMM.length > 0)
							{
								SearchResult searchResultFromUser = this.showOptionPane(searchResultsDMM, "dmm.co.jp");
								if(searchResultFromUser == null)
									return;
								overrideURLDMM = searchResultFromUser.getUrlPath();

							}
							SearchResult [] searchResultsJavLibStrings = jlPP.getSearchResults(searchStringJL);
							if(searchResultsJavLibStrings != null && searchResultsJavLibStrings.length > 0)
							{
								SearchResult searchResultFromUser = this.showOptionPane(searchResultsJavLibStrings, "javlibrary.com");
								if(searchResultFromUser == null)
									return;
								overrideURLJavLibrary = searchResultFromUser.getUrlPath();				
							}
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}

					else if(promptUserForURLWhenScraping && (scrapeData18Movie || scrapeData18WebContent))
					{
						try {
							SiteParsingProfile data18ParsingProfile = null;
							if(scrapeData18Movie)
								data18ParsingProfile = new Data18MovieParsingProfile();
							else if(scrapeData18WebContent)
								data18ParsingProfile = new Data18WebContentParsingProfile();
							String searchStringData18Movie = data18ParsingProfile.createSearchString(currentlySelectedMovieFileList.get(movieNumberInList));
							SearchResult [] searchResultData18Movie = data18ParsingProfile.getSearchResults(searchStringData18Movie);
							if(searchResultData18Movie != null && searchResultData18Movie.length > 0)
							{
								SearchResult searchResultFromUser = this.showOptionPane(searchResultData18Movie, "Data18 Movie");
								if(searchResultFromUser == null)
									return;
								overrideURLData18Movie = searchResultFromUser.getUrlPath();

							}
							
							if( scrapeData18Movie && preferences.getUseIAFDForActors() ) {
								IAFDParsingProfile iafdParsingProfile = new IAFDParsingProfile();
								SearchResult[] searchResultsIAFD = iafdParsingProfile.getSearchResults(iafdParsingProfile.createSearchString(currentlySelectedMovieFileList.get(movieNumberInList)));
								System.out.println("neues");
								System.out.println(searchStringData18Movie);
								System.out.println(searchResultsIAFD);
								
								if(searchResultsIAFD != null && searchResultsIAFD.length > 0)
								{
									SearchResult searchResultFromUser = this.showOptionPane(searchResultsIAFD, "Data18 Movie");
									if(searchResultFromUser == null)
										return;
									overrideURLIAFD = searchResultFromUser.getUrlPath();
									if (!overrideURLIAFD.contains("iafd.com"))
										overrideURLIAFD = "http://www.iafd.com/" + overrideURLIAFD;
									System.out.println("Neue URL " + overrideURLIAFD);
									
								}
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					Movie javMovie = null;
					Movie data18Movie = null;
					if(scrapeJAV)
						javMovie = makeJavThreadsAndScrape(movieNumberInList);
					else if(scrapeData18Movie)
						data18Movie = makeData18MovieThreadsAndScrape(movieNumberInList, true);
					else if(scrapeData18WebContent)
					{
						data18Movie = makeData18MovieThreadsAndScrape(movieNumberInList, false);
					}

					//Allow the user to manually pick poster from a dialog box for data18 movies
					if(manuallyPickPoster && data18Movie != null && data18Movie.getPosters() != null && data18Movie.getPosters().length > 1)
					{
						//get all unique elements from the posters and the extrafanart - my method here is probably pretty inefficient, but the lists aren't more than 100 items, so no big deal
						HashSet<Thumb> uniqueElements = new HashSet<Thumb>(Arrays.asList(data18Movie.getPosters()));
						uniqueElements.addAll(Arrays.asList(data18Movie.getExtraFanart()));
						ArrayList<Thumb> uniqueElementsList = (new ArrayList<Thumb>(uniqueElements));
						Thumb [] uniqueElementsArray = uniqueElementsList.toArray(new Thumb[uniqueElementsList.size()]);

						Thumb posterPicked = showArtPicker(uniqueElementsArray,"Pick Poster");
						if(posterPicked != null)
						{
							//remove the item from the picked from the existing poster and put it at the front of the list
							ArrayList<Thumb> existingPosters = new ArrayList<Thumb>(Arrays.asList(currentlySelectedMovieData18Movie.getPosters()));
							existingPosters.remove(posterPicked);
							existingPosters.add(0,posterPicked);
							Thumb [] posterArray = new Thumb[existingPosters.size()];
							currentlySelectedMovieData18Movie.setPosters(existingPosters.toArray(posterArray));
						}
					}

					//Allow the user to manually pick fanart from a dialog box
					if(manuallyPickFanart && data18Movie != null && data18Movie.getFanart() != null && data18Movie.getFanart().length > 1)
					{
						//get all unique elements from the fanart and the extrafanart - my method here is probably pretty inefficient, but the lists aren't more than 100 items, so no big deal
						HashSet<Thumb> uniqueElements = new HashSet<Thumb>(Arrays.asList(data18Movie.getFanart()));
						uniqueElements.addAll(Arrays.asList(data18Movie.getExtraFanart()));
						ArrayList<Thumb> uniqueElementsList = (new ArrayList<Thumb>(uniqueElements));
						Thumb [] uniqueElementsArray = uniqueElementsList.toArray(new Thumb[uniqueElementsList.size()]);

						Thumb fanartPicked = showArtPicker(uniqueElementsArray,"Pick Fanart");
						if(fanartPicked != null)
						{
							//remove the item from the picked from the existing fanart and put it at the front of the list
							ArrayList<Thumb> existingFanart = new ArrayList<Thumb>(Arrays.asList(currentlySelectedMovieData18Movie.getFanart()));
							existingFanart.remove(fanartPicked);
							existingFanart.add(0,fanartPicked);
							Thumb [] fanartArray = new Thumb[existingFanart.size()];
							currentlySelectedMovieData18Movie.setFanart(existingFanart.toArray(fanartArray));
						}
					}

					else if(manuallyPickFanart && javMovie != null)
					{
						//we don't need to worry about picking out the unique elements like above since there is no duplication between fanart and extrafanart in this case
						Thumb fanartPicked = showArtPicker(ArrayUtils.addAll(javMovie.getFanart(), javMovie.getExtraFanart()),"Pick Fanart");
						if(fanartPicked != null)
							javMovie.setFanart(ArrayUtils.toArray(fanartPicked));
					}

					if(movieToWriteToDiskList == null || movieToWriteToDiskList.get(movieNumberInList) == null)
					{
						System.out.println("No movie result found");
						JOptionPane.showMessageDialog(frmMoviescraper, "Could not find any movies that match the selected file while scraping.", "No Movies Found", JOptionPane.ERROR_MESSAGE, null);
					}
					//Let's clear out the actorsFolder so we can get new images from the scraped results instead of relying on whatever is there locally
					//currentlySelectedActorsFolderList.clear();

					//if(movieToWriteToDiskList != null && movieToWriteToDiskList.get(movieNumberInList) != null)
					//updateAllFieldsOfSite1Movie();
				}
			}
			finally
			{
				clearOverrides();
				//by calling this with the parameter of true, we'll force a refresh from the URL not just update the poster from the file on disk
				updateAllFieldsOfSite1Movie(true);
				frmMoviescraper.setCursor(Cursor.getDefaultCursor());
			}
		}


		private void clearOverrides() {
			this.overrideURLData18Movie = "";
			this.overrideURLDMM = "";
			this.overrideURLJavLibrary = "";
			
		}

		//Selection Dialog box used to display posters and fanarts
		public Thumb showArtPicker(Thumb [] thumbArray, String windowTitle)
		{
			if(thumbArray.length > 0)
			{
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());
				JList<Thumb> labelList = new JList<Thumb>(thumbArray);
				labelList.setCellRenderer(new FanartPickerRenderer());
				labelList.setVisible(true);
				JScrollPane pane = new JScrollPane(labelList);
				panel.add(pane, BorderLayout.CENTER);
				panel.setPreferredSize(new Dimension(325,600));
				
				final JDialog bwin = new JDialog();
		         bwin.addWindowFocusListener(new WindowFocusListener()
		         {
		             @Override
		             public void windowLostFocus(WindowEvent e)
		             {
		               bwin.setVisible(false);
		               bwin.dispose();
		             }

		             @Override
		             public void windowGainedFocus(WindowEvent e)
		             {
		             }
		         }); 
		         bwin.add(panel);
		         bwin.pack();
				
				int result = JOptionPane.showOptionDialog(null, panel, windowTitle,
		                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
		                null, null, null);
				if(result == JOptionPane.OK_OPTION)
				{
					Thumb optionPickedFromPanel = labelList.getSelectedValue();
					return optionPickedFromPanel;
				}
				else return null;
			}
			else return null;
		}


		private Movie makeData18MovieThreadsAndScrape(int movieNumberInList, boolean isData18Movie) {
			//we need to create a final copy of the loop variable to pass it into each run method and make the compiler happy
			final int currentMovieNumberInList = movieNumberInList;
			final boolean parsingType = isData18Movie;
			Thread scrapeQueryData18MovieThread = new Thread() {
				public void run() {
					try {
						SiteParsingProfile data18MoviePP;
						if(parsingType)
							data18MoviePP = new Data18MovieParsingProfile();
						else
							data18MoviePP = new Data18WebContentParsingProfile();
						//data18MoviePP.setExtraFanartScrapingEnabled(preferences.getExtraFanartScrapingEnabledPreference());
						debugWriter("Scraping this file (Data18) " + currentlySelectedMovieFileList.get(currentMovieNumberInList));
						currentlySelectedMovieData18Movie = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								data18MoviePP, overrideURLData18Movie, promptUserForURLWhenScraping);

						System.out.println("Data18 Scrape results: "
								+ currentlySelectedMovieData18Movie);
						
						if ( preferences.getUseIAFDForActors() ) {
							Movie scrapeMovieIAFD = Movie.scrapeMovie(
									currentlySelectedMovieFileList.get(currentMovieNumberInList),
									new IAFDParsingProfile(), overrideURLIAFD, promptUserForURLWhenScraping);
							System.out.println("IAFD Scrape results: "
									+ scrapeMovieIAFD);
							
							currentlySelectedMovieData18Movie.setActors( scrapeMovieIAFD.getActors() );
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};

			try
			{
				scrapeQueryData18MovieThread.start();
				scrapeQueryData18MovieThread.join();

				movieToWriteToDiskList.add(currentlySelectedMovieData18Movie);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return currentlySelectedMovieData18Movie;
		}


		private Movie makeJavThreadsAndScrape(int movieNumberInList) {
			//we need to create a final copy of the loop variable to pass it into each run method and make the compiler happy
			final int currentMovieNumberInList = movieNumberInList;
			Movie movieAmalgamated = null;
			// Scape dmm.co.jp for currently selected movie
			Thread scrapeQueryDMMThread = new Thread() {
				public void run() {
					try {
						DmmParsingProfile dmmPP = new DmmParsingProfile();
						dmmPP.setExtraFanartScrapingEnabled(preferences.getExtraFanartScrapingEnabledPreference());
						currentlySelectedMovieDMM = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								dmmPP, overrideURLDMM, promptUserForURLWhenScraping);

						System.out.println("DMM scrape results: "
								+ currentlySelectedMovieDMM);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};
			// Scrape ActionJav.com for currently selected movie
			Thread scrapeQueryActionJavThread = new Thread() {
				public void run() {
					try {
						currentlySelectedMovieActionJav = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								new ActionJavParsingProfile(), overrideURLDMM, false);

						System.out.println("Action jav scrape results: "
								+ currentlySelectedMovieActionJav);

					} catch (IOException e1) {

						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};

			// Scrape SquarePlus.co.jp for currently selected movie
			Thread scrapeQuerySquarePlusThread = new Thread() {
				public void run() {
					try {
						currentlySelectedMovieSquarePlus = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								new SquarePlusParsingProfile(), overrideURLDMM, false);

						System.out.println("SquarePlus scrape results: "
								+ currentlySelectedMovieSquarePlus);

					} catch (IOException e1) {

						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};

			// Scrape JavLibrary for currently selected movie
			Thread scrapeQueryJavLibraryThread = new Thread() {
				public void run() {
					try {
						JavLibraryParsingProfile jlParsingProfile = new JavLibraryParsingProfile();
						jlParsingProfile.setOverrideURLJavLibrary(overrideURLJavLibrary);
						currentlySelectedMovieJavLibrary = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								jlParsingProfile, overrideURLDMM, promptUserForURLWhenScraping);

						System.out.println("JavLibrary scrape results: "
								+ currentlySelectedMovieJavLibrary);

					} catch (IOException e1) {

						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};

			Thread scrapeQueryJavZooThread = new Thread() {
				public void run() {
					try {
						currentlySelectedMovieJavZoo = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								new JavZooParsingProfile(), overrideURLDMM, false);

						System.out.println("JavZoo scrape results: "
								+ currentlySelectedMovieJavZoo);

					} catch (IOException e1) {

						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};


			Thread scrapeQueryCaribbeancomPremium = new Thread() {
				public void run() {
					try {
						currentlySelectedMovieCaribbeancomPremium = Movie.scrapeMovie(
								currentlySelectedMovieFileList.get(currentMovieNumberInList),
								new CaribbeancomPremiumParsingProfile(), overrideURLDMM, false);

						System.out.println("CaribbeancomPremium scrape results: "
								+ currentlySelectedMovieCaribbeancomPremium);

					} catch (IOException e1) {

						e1.printStackTrace();
						//JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
					}
				}
			};


			try
			{
				// Run all the threads in parallel
				scrapeQueryDMMThread.start();
				scrapeQueryActionJavThread.start();
				scrapeQuerySquarePlusThread.start();
				scrapeQueryJavLibraryThread.start();
				scrapeQueryJavZooThread.start();
				scrapeQueryCaribbeancomPremium.start();


				// wait for them to finish before updating gui

				scrapeQueryJavLibraryThread.join();
				scrapeQueryDMMThread.join();
				scrapeQueryActionJavThread.join();
				scrapeQuerySquarePlusThread.join();
				scrapeQueryJavZooThread.join();
				scrapeQueryCaribbeancomPremium.join();
				movieAmalgamated = amalgamateMovie(currentlySelectedMovieDMM,
						currentlySelectedMovieActionJav,
						currentlySelectedMovieSquarePlus,
						currentlySelectedMovieJavLibrary,
						currentlySelectedMovieJavZoo, movieNumberInList);
				//if we didn't get a result from the general jav db's, then maybe this is from a webonly type scraper
				if(movieAmalgamated == null && currentlySelectedMovieCaribbeancomPremium != null)
					movieAmalgamated = currentlySelectedMovieCaribbeancomPremium;
				if(movieAmalgamated != null)
				{
					movieToWriteToDiskList.add(movieAmalgamated);
				}
			}
			catch (InterruptedException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(e1),"Unhandled Exception",JOptionPane.ERROR_MESSAGE);
			}
			return movieAmalgamated;
		}
		
	}
	private class ScrapeMovieActionAutomatic extends ScrapeMovieAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ScrapeMovieActionAutomatic()
		{
			super();
			putValue(NAME, "Scrape JAV (Automatic)");
			putValue(SHORT_DESCRIPTION, "Scrape Selected Movie (Automatic)");
			promptUserForURLWhenScraping = false;
			manuallyPickFanart = false;
		}
		public void actionPerformed(ActionEvent e){
			super.actionPerformed(e);
		}
	}
	
	private class ScrapeMovieActionData18Movie extends ScrapeMovieAction
	{
		private static final long serialVersionUID = 1L;

		public ScrapeMovieActionData18Movie()
		 {
			super();
			putValue(NAME, "Scrape Data18 Movie");
			putValue(SHORT_DESCRIPTION, "Scrape Data18 Movie");
			promptUserForURLWhenScraping = true;
			this.scrapeData18Movie = true;
			this.scrapeJAV = false;
		 }
		public void actionPerformed(ActionEvent e){
			super.actionPerformed(e);
		}
	}
	
	private class ScrapeMovieActionData18WebContent extends ScrapeMovieAction
	{
		private static final long serialVersionUID = 1L;

		public ScrapeMovieActionData18WebContent()
		 {
			super();
			putValue(NAME, "Scrape Data18 WebContent");
			putValue(SHORT_DESCRIPTION, "Scrape Data18 WebContent");
			promptUserForURLWhenScraping = true;
			this.scrapeData18Movie = false;
			this.scrapeJAV = false;
			this.scrapeData18WebContent = true;
		 }
		public void actionPerformed(ActionEvent e){
			super.actionPerformed(e);
		}
	}
	class FileList {

		public JScrollPane getGui(File[] all,
				DefaultListModel<File> listModelFiles, boolean vertical) {

			//Gotta clear out the old list before we can populate it with new stuff
			fileList.removeAll();

			for (File file : all) {
				listModelFiles.addElement(file);
			}
			// ..then use a renderer
			fileList.setCellRenderer(new FileRenderer(!vertical));

			if (!vertical) {
				fileList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
				fileList.setVisibleRowCount(-1);
			} else {
				fileList.setVisibleRowCount(9);
			}
			return new JScrollPane(fileList);
		}
	}

	class FileRenderer extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean pad;
		private Border padBorder = new EmptyBorder(3, 3, 3, 3);
		

		FileRenderer(boolean pad) {
			this.pad = pad;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			Component c = super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			JLabel l = (JLabel) c;
			File f = (File) value;
			l.setText(f.getName());
			try {
				l.setIcon(IconCache.getIconFromCache(f));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Setting Icon at " + System.currentTimeMillis());
			//l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
			
			if (pad) {
				l.setBorder(padBorder);
			}

			return l;
		}
	}

	public class ActressListRenderer extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Font font = new Font("helvitica", Font.BOLD, 12);

		public ActressListRenderer() {
			setBorder(new EmptyBorder(1, 1, 1, 1));
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);
			label.setIcon(getImageIconForLabelName());
			label.setHorizontalTextPosition(JLabel.RIGHT);
			label.setFont(font);
			label.setBorder(BorderFactory.createLineBorder(Color.black));
			if (index % 2 == 0) {
				label.setBackground(SystemColor.controlShadow);
			} else {
				label.setBackground(SystemColor.controlHighlight);
			}
			return label;
		}
		
		//TODO: I should probably re-implement this to use Maps instead of arrays
		//TODO: Store the files from .actor in a cache somewhere
		private ImageIcon getImageIconForLabelName() {
			if (movieToWriteToDiskList != null && movieToWriteToDiskList.size() > 0) {
				for (Actor currentActor : movieToWriteToDiskList.get(0).getActors()) {
					if (this.getText().equals(currentActor.getName())) {
						if (currentActor.getThumb() != null)
						{
							//see if we can find a local copy in the .actors folder before trying to download
							if(currentlySelectedActorsFolderList != null && currentlySelectedActorsFolderList.get(0).isDirectory())
							{
								String currentActorNameAsPotentialFileName = currentActor.getName().replace(' ', '_');
								File [] listFiles = currentlySelectedActorsFolderList.get(0).listFiles();
								for(File currentFile : listFiles)
								{
									if(currentFile.isFile() && FilenameUtils.removeExtension(currentFile.getName()).equals(currentActorNameAsPotentialFileName)){										
										return new ImageIcon(currentFile.getPath());
									}
								}
							}

							else 
							{
								return currentActor.getThumb().getImageIconThumbImage();
							}
						}
						else
							return new ImageIcon();
					}
				}
			}
			return new ImageIcon();
		}
	}

	public void updateActorsFolder() {
		for(int movieNumberInList = 0; movieNumberInList < currentlySelectedMovieFileList.size(); movieNumberInList++)
		{
			if(currentlySelectedMovieFileList.get(movieNumberInList).isDirectory())
			{
				currentlySelectedActorsFolderList.add(new File(currentlySelectedMovieFileList.get(movieNumberInList).getPath() + "\\.actors"));
			}
			else if(currentlySelectedMovieFileList.get(movieNumberInList).isFile())
			{
				currentlySelectedActorsFolderList.add(new File(currentlySelectedDirectoryList.getPath() + "\\.actors"));
			}
		}
	}
	
	public void updateExtraFanartFolder(File destinationDirectory){
		for(int movieNumberInList = 0; movieNumberInList < currentlySelectedMovieFileList.size(); movieNumberInList++)
			{
			if(destinationDirectory != null)
			{
				currentlySelectedExtraFanartFolderList.add(new File(destinationDirectory.getPath() + "\\extrafanart"));
			}	
			else if(currentlySelectedMovieFileList.get(movieNumberInList).isDirectory())
			{
				
				currentlySelectedExtraFanartFolderList.add(new File(currentlySelectedMovieFileList.get(movieNumberInList).getPath() + "\\extrafanart"));
			}
			else
			{
				//do nothing for now. this may be a bug with selecting folders and files at the same time, so i may need to revist this later
				//currentlySelectedExtraFanartFolderList = null;
			}
		}
	}

	public File[] actorFolderFiles(int movieNumberInList) {
		ArrayList<File> actorFiles = new ArrayList<File>();
		if(movieToWriteToDiskList != null && movieToWriteToDiskList.size() > 0 && movieToWriteToDiskList.get(movieNumberInList).getActors() != null)
		{
			if(currentlySelectedActorsFolderList != null && currentlySelectedActorsFolderList.get(movieNumberInList).isDirectory())
			{
				for (Actor currentActor : movieToWriteToDiskList.get(movieNumberInList).getActors())
				{
					String currentActorNameAsPotentialFileName = currentActor.getName().replace(' ', '_');
					File [] listFiles = currentlySelectedActorsFolderList.get(movieNumberInList).listFiles();
					for(File currentFile : listFiles)
					{
						if(currentFile.isFile() && FilenameUtils.removeExtension(currentFile.getName()).equals(currentActorNameAsPotentialFileName)){										
							actorFiles.add(currentFile);
						}
					}
				}
			}
		}
		return actorFiles.toArray(new File[actorFiles.size()]);
	}

	private void writeExtraFanart(File destinationDirectory, int movieNumberInList) throws IOException {
		updateExtraFanartFolder(destinationDirectory);
		if(movieToWriteToDiskList != null && movieToWriteToDiskList.size() > 0 && movieToWriteToDiskList.get(movieNumberInList).getExtraFanart() != null && movieToWriteToDiskList.get(movieNumberInList).getExtraFanart().length > 0)
		{
			FileUtils.forceMkdir(currentlySelectedExtraFanartFolderList.get(movieNumberInList));
			int currentExtraFanartNumber = 1;
			for(Thumb currentExtraFanart : movieToWriteToDiskList.get(movieNumberInList).getExtraFanart())
			{
				File fileNameToWrite = new File(currentlySelectedExtraFanartFolderList.get(movieNumberInList).getPath() + "\\" + "fanart" + currentExtraFanartNumber + ".jpg");
				
				//no need to overwrite perfectly good extra fanart since this stuff doesn't change. this will also save time when rescraping since extra IO isn't done.
				if(!fileNameToWrite.exists())
				{
					System.out.println("Writing extrafanart to " + fileNameToWrite);
					currentExtraFanart.writeImageToFile(fileNameToWrite);
				}
				currentExtraFanartNumber++;
			}
		}
	}
	
	private class UpDirectoryAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				File parentDirectory = currentlySelectedDirectoryList.getParentFile();
				if(parentDirectory != null && parentDirectory.exists())
				{
					currentlySelectedDirectoryList = parentDirectory;
					frmMoviescraper.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					updateFileListModel(currentlySelectedDirectoryList);
				}
			}
			finally
			{
				preferences.setLastUsedDirectory(currentlySelectedDirectoryList);
				frmMoviescraper.setCursor(Cursor.getDefaultCursor());
			}

		}

	}
	
	public FileDetailPanel getFileDetailPanel() {
		return fileDetailPanel;
	}

	public File getCurrentFile() {
		if (currentlySelectedMovieFileList.size() > 0)
			return currentlySelectedMovieFileList.get(0);
		return null;
	}

}

