import java.io.*;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class ed extends JFrame implements ActionListener
{
	JFrame frame1;
	JTable table;
	JButton enc,dec,his;
	JFileChooser saveFile;
	String path="";
	int ch;
	String txt="";
	int i=0;
	int l=0;
	String temp="";
	String data="";
	String enc_str="";
	String dec_str="";
	String enc_data="";
	String dec_data="";
	String test="this test : ";
	String key_in ="";
	int key[]=new int[5];
	String columnNames[]={"File Path","Date Of Encryption","Size of File","Date Of Decryption"};
	String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
	String url = "jdbc:odbc:eddb";//name of dsn is eddb
	DateFormat sdf;
	Date date;	
	String encdate="-";
	String decdate="-";
	String sizefile="-";
	String filepathstr="-";
	public ed()
	{
		setVisible(true);
		setSize(500,500);
		setTitle("Encryption-Decryption");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		enc=new JButton("Encrypt");
		enc.setBounds(50,50,50,50);
		enc.setBackground(Color.blue);
		add(enc);
		dec=new JButton("Decrypt");
		dec.setBackground(Color.red);
		add(dec);
		his=new JButton("History");
		his.setBackground(Color.gray);
		add(his);
		enc.addActionListener(this);
		dec.addActionListener(this);
		his.addActionListener(this);
		saveFile = new JFileChooser();
		key[0] = 0;
		key[1] = 0;
		key[2] = 0;
		key[3] = 0;
		key[4] = 0;
		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}
	public void actionPerformed(ActionEvent e)
	{
		String str=e.getActionCommand();
		if(str.equals("Encrypt"))
			{			
				key_in=JOptionPane.showInputDialog(null,"Enter the key of 5 charcter ","Key",1);
				String key_text="";
				for(i=0;i<5;i++)
				{
					key[i]= Character.getNumericValue(key_in.charAt(i));								
					key[i]=key[i]%10;
					key_text=key_text+Integer.toString(key[i]);
				}							
				if(key_in!=null)
				{
                saveFile.showSaveDialog(null);
                path=saveFile.getSelectedFile().toString();
                filepathstr=path;                             
                enc_data=null;
                enc_data=file_read(path);               
                enc_str=null;
                enc_str=enc(enc_data,key);
                file_write(path,enc_str);
                date = new Date();
                encdate=sdf.format(date);       
                insertDB(filepathstr,encdate,sizefile,decdate);
                JOptionPane.showMessageDialog(null,"File has been Encrypted.");
                }
			}
		if(str.equals("Decrypt"))
			{               
				key_in=JOptionPane.showInputDialog(null,"Enter the key of 5 charcter ","Key",1);				
				for(i=0;i<5;i++)
				{
					key[i]= Character.getNumericValue(key_in.charAt(i));
				}
				if(key_in!=null)
				{
                saveFile.showSaveDialog(null);
                path=saveFile.getSelectedFile().toString();
                filepathstr=path;
                dec_data=null;               
                dec_data=file_read(path);                              
                dec_str=null;
                dec_str=dec(dec_data,key); 
                file_write(path,dec_str);
                date = new Date();
                decdate=sdf.format(date);
                filepathstr=path;
                updateDB();
                JOptionPane.showMessageDialog(null,"File has been Decrypted.");
                }               
			}
			if(str.equals("History"))
			{
				showData();
			}
	}

	public String file_read(String path)
	{
		try
		{			
			FileInputStream fis=new FileInputStream(path);
			File f=new File(path);
			double s=f.length();
			int size=(int)s;
			byte[] buffer=new byte[1000];
			int read=0;
			String temp_txt;
            sizefile=Long.toString(f.length());
			while((read=fis.read(buffer))!=-1)
			{
				temp_txt=new String(buffer);
				txt=txt+temp_txt;
			}			
			fis.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return txt;
	}
	public void file_write(String path,String data)
	{
		try
		{			
			byte[] buffer=data.getBytes();
			FileOutputStream fos=new FileOutputStream(path);
			fos.flush();
			fos.write(buffer);
			fos.close();			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public String enc(String txt,int k[])
	{
		int i,j;
		int l=txt.length();
		int key[]=new int[l];
		char txt_char[]=new char[l];
		int txt_int[]=new int[l];
		int cipher_int[]=new int[l];
		char cipher_char[]=new char[l];
		for(i=0;i<5;i++)
		{
			key[i] = k[i];	
		}		
		if(l>5)
		{
			for(i=5;i<l;i++)
			{
            	for(j=i-5;j<i-4;j++)
            	{
            		key[i]=key[j];
            	}
			}
		}	
		for(i=0;i<l;i++)
		{		
			txt_int[i]=(int)txt.charAt(i);
			cipher_int[i]=txt_int[i]+key[i];
			cipher_char[i]=(char)cipher_int[i];
		}
		txt=new String(cipher_char);
		return txt;
	}
	public String dec(String txt,int k[])
	{

		int i,j;
		int l=txt.length();
		int key[]=new int[l];
		char txt_char[]=new char[l];
		int txt_int[]=new int[l];
		int cipher_int[]=new int[l];
		char cipher_char[]=new char[l];
		for(i=0;i<5;i++)
		{
			key[i] = k[i];	
		}		
		if(l>5)
		{
			for(i=5;i<l;i++)
			{
            	for(j=i-5;j<i-4;j++)
            	{
            		key[i]=key[j];
            	}
			}
		}	

		for(i=0;i<l;i++)
		{
			cipher_int[i]=(int)txt.charAt(i);
			txt_int[i]=cipher_int[i]-key[i];
			txt_char[i]=(char)txt_int[i];
		}
		txt=new String(txt_char);
		return txt;
	}
	void insertDB(String filepathstr,String encdate,String sizefile,String decdate)
	{
		try
		{ 
			Class.forName(driverName); 
			Connection con = DriverManager.getConnection(url);
			//Statement s=con.createStatement();
			Statement si=con.createStatement();
			si.executeUpdate("insert into ed values('"+filepathstr+"','"+encdate+"','"+sizefile+"','"+decdate+"')");			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	void updateDB()
	{
		try
		{ 
			Class.forName(driverName); 
			Connection con = DriverManager.getConnection(url);
			Statement s=con.createStatement();
			Statement si=con.createStatement();
			String sql = "select * from ed";//Name of Table is ed
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String chk=filepathstr;	
			s.executeUpdate("update ed set dateofdec='"+decdate+"' where filename='"+chk+"'");			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}	
	void showData()
	{
		frame1 = new JFrame("History");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setLayout(new BorderLayout()); 
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		table = new JTable();
		table.setModel(model); 
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		try
		{ 
			Class.forName(driverName); 
			//Connection con = DriverManager.getConnection(url, userName, password);
			Connection con = DriverManager.getConnection(url);
			String sql = "select * from ed";//Name of Table is ed
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int i =0;
			while(rs.next())
			{
				filepathstr = rs.getString("filename");
				encdate = rs.getString("dateofenc");
				sizefile = rs.getString("sizeoffile"); 
				decdate = rs.getString("dateofdec");
				model.addRow(new Object[]{filepathstr,encdate,sizefile+"Bytes",decdate});
				i++; 
			}
			if(i <1)
			{
				JOptionPane.showMessageDialog(null, "No Record Found","Error",JOptionPane.ERROR_MESSAGE);
			}
			if(i ==1)
			{
				System.out.println(i+" Record Found");
			}
			else
			{
				System.out.println(i+" Records Found");
			}
		}
		catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
		frame1.add(scroll);
		frame1.setVisible(true);
		frame1.setSize(400,300);
	}
	public static void main(String args[])
	{
		new ed();
	}
} 
/*
dsn name : eddb
table name : ed
column1 : filename
column2 : dateofenc
column3 : sizeoffile
column4 : dateofdec
*/
