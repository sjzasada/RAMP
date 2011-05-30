package uk.ac.ucl.chem.ccs.ramp.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import uk.ac.ucl.chem.ccs.ramp.rfq.Request;

import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;
import javax.swing.JTextArea;

public class UserGui extends JFrame {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextArea textArea;
	private UserAgent myAgent=null;
	private Vector<Request> loadedRequests;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UserGui dialog = new UserGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public UserGui(UserAgent a) {
		this();
		myAgent=a;
	}
	
	/**
	 * Create the dialog.
	 */
	public UserGui() {
		
		loadedRequests = new Vector<Request>();
		
		setBounds(100, 100, 799, 572);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 763, 236);
		contentPanel.add(scrollPane);

		this.setTitle("User Agent GUI");
		
		table = new JTable();


		
		
		scrollPane.setViewportView(table);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 258, 783, 41);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			
			JButton btnNewButton = new JButton("Load");
			btnNewButton.setBounds(10, 0, 87, 30);
			buttonPane.add(btnNewButton);
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
					int returnVal = fc.showOpenDialog(UserGui.this);

					
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
											
						loadDir(fc.getSelectedFile().getAbsolutePath());
	
					}
				}

			});
			
			JButton btnPurchase = new JButton("Purchase");
			btnPurchase.setBounds(107, 0, 87, 30);
			btnPurchase.addActionListener(new ActionListener () {
				public void actionPerformed (ActionEvent e) {
					
					
					if (table.getSelectedRowCount() > 1) {
						
						int index[] = table.getSelectedRows();
						
						
						//do this if more than one row is selected
						Vector<Request> vec = new Vector<Request>();
						
						for (int i=0; i<index.length; i++) {
							vec.add(loadedRequests.elementAt(index[i]));
						}
						
						if (myAgent != null) {
							myAgent.requestQuote(vec);
						}
						
					} else if (table.getSelectedRowCount() == 1) {
						
						int index = table.getSelectedRow();
						
						Request req = loadedRequests.get(index);
						
						if (myAgent != null) {
							myAgent.requestQuote(req);
						}
						
					}
					

				}
			});
			
			buttonPane.add(btnPurchase);
						
			JButton cancelButton = new JButton("Close");
			cancelButton.setBounds(703, 0, 70, 30);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (myAgent != null) {
						myAgent.takeDown();
						UserGui.this.dispose();
					} else {					
						System.exit(0); //TODO: find out how to close properly
					}
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 310, 763, 213);
		contentPanel.add(scrollPane_1);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane_1.setViewportView(textArea);
		
		updateDisplay ();
		
	}
	
	public void loadDir(String dir) {
		
		File fo = new File(dir);
		
		if(fo.isDirectory()) {
			String internalNames[] = fo.list();
			for(int i=0; i<internalNames.length; i++) {
				loadFile(fo.getAbsolutePath() + File.separator + internalNames[i]);
			} 

			
		} else {
			loadFile(fo.getAbsolutePath());
		}
		updateDisplay();

	}
	
	public void loadFile (String file) {
		Request req = new Request();
		
		if (req.load(file)) {
			loadedRequests.add(req);
		} //TODO: Pop up dialogue if fail
	}
	
	
	public void writeMessage (String s) {
		textArea.append(s+"\n");
	}
	
	public void updateDisplay () {
		
		if (loadedRequests.size() > 0) {
			
			Object data[][] = new Object[loadedRequests.size()][4];
			int row=0;
			for (Request req:loadedRequests) {
				data[row][0]=req.getStart();
				data[row][1]=req.getEnd();
				data[row][2]=req.getCPUCost();
				data[row][3]=req.getCPUCount();
				row++;
			}
			
			
			DefaultTableModel dtm2 = new DefaultTableModel(data,
			        new String[] { "Start", "End", "Cost", "CPU Count" });
			
			table.setModel(dtm2);
			table.setFillsViewportHeight(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		} else {
		
		//set up table
		DefaultTableModel dtm2 = new DefaultTableModel(new String[][] { { "", "", "", "" } },
		        new String[] { "Start", "End", "Cost", "CPU Count" });
		
		table.setModel(dtm2);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		}
		
	}
}
