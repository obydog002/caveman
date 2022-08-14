package src.file;

import java.io.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// a class to manage file types used for caveman
public class FileManager
{
	// path to res folder
	public static final String RES_PATH = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/res/";
	
	// caveman level header
	//public static final byte[] CVL_HEADER = ("\221" + "CVL\r\n" + "\032" + "\n").getBytes();
	public static final byte[] CVL_HEADER = {0x3f, 0x43, 0x56, 0x4c, 0x0d, 0x0a, 0x1a, 0x0a};
	// caveman campaign save header
	public static final byte[] SAV_HEADER = ("\221" + "SAV\r\n" + "\032" + "\n").getBytes();
	
	// file versions
	public static void write_cv_level(File file, int level_width, int level_height, byte[] entities, String level_name)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream writer = new BufferedOutputStream(fos);
			
			// write header, 1 bytes for level_width, 1 bytes for level_height, 
			// width*height level bytes, 
			// 1 bytes for name length, length*bytes for name, 
			
			writer.write(CVL_HEADER);
			writer.write(level_width);
			writer.write(level_height);
			writer.write(entities);
			
			// level name should not be more than 255 characters!
			writer.write(level_name.length());
			writer.write(level_name.getBytes());
			
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Level read_cv_level(File file)
	{
		try
		{
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream reader = new BufferedInputStream(fin);
			
			int b;
			boolean header_match = true;
			for (int i = 0; i < 8; i++)
			{
				b = reader.read();
				header_match &= (b == CVL_HEADER[i]);
			}
			
			// check if header is right
			if (!header_match)
				throw new Exception(file.getName() + " is not a caveman level file!");
			
			if ((b = reader.read()) == -1)
				throw new Exception("Could not read width!");
			int width = b;
			
			if ((b = reader.read()) == -1)
				throw new Exception("Could not read height!");
			int height = b;
			
			int[] entities = new int[width*height];
			for (int i = 0; i < width*height; i++)
			{
				if ((b = reader.read()) == -1)
					throw new Exception("Could not read entities");
				
				entities[i] = b;
			}
			
			if ((b = reader.read()) == -1)
				return null;
			
			int name_length = b;
			String level_name = "";
			
			while ((b = reader.read()) != -1)
			{
				char c = (char)b;
				level_name += c;
			}
			
			if (level_name.length() != name_length)
				throw new Exception("Unexpected name length!");
			
			return new Level(width, height, entities, level_name);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// string file and path versions
	public static void write_cv_level(String path, String filename, int level_width, int level_height, byte[] entities, String level_name)
	{
		write_cv_level(new File(path + filename), level_width, level_height, entities, level_name);
	}
	
	public static Level read_cv_level(String path, String filename)
	{
		return read_cv_level(new File(path + filename));
	}
	
	// writing, reading campaign files
	
	public static void write_campaign_save_file(File file, byte[] campaign_marker, int level, String name)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream writer = new BufferedOutputStream(fos);
			
			// write header 8 bytes
			// 4 bytes campaign_marker
			// 1 byte level number (LESS THAN 256!)
			// 1 byte string length (< 256), length bytes name
			
			writer.write(SAV_HEADER);
			writer.write(campaign_marker);
			
			writer.write(level);
			
			writer.write(name.length());
			writer.write(name.getBytes());
			
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static CampaignSave read_campaign_save_file(File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream reader = new BufferedInputStream(fis);
			
			int b;
			boolean header_match = true;
			for (int i = 0; i < 8; i++)
			{
				b = reader.read();
				header_match &= (b == SAV_HEADER[i]);
			}
			
			// check if header is right
			if (!header_match)
				return null;
			
			// read campaign marker
			byte[] campaign_marker = new byte[4];
			for (int i = 0; i < 4; i++)
			{
				b = reader.read();
				if (b == -1)
					throw new RuntimeException("Error in reading campaign file!");
				
				campaign_marker[i] = (byte)b;
			}
		
			
			if ((b = reader.read()) == -1)
				throw new RuntimeException("Error in reading campaign file!");
			
			int level = b;
			
			if ((b = reader.read()) == -1)
				throw new RuntimeException("Error in reading campaign file!");
			
			int name_length = b;
			
			String name = "";
			
			while ((b = reader.read()) != -1)
			{
				char c = (char)b;
				name += c;
				//System.out.print(c);
			}
			
			if (name.length() != name_length)
				throw new RuntimeException("Error in reading campaign file!");
			
			return new CampaignSave(campaign_marker, level, name);
			
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
	}
	
	public static CampaignSave read_campaign_save_file(String path, String filename)
	{
		return read_campaign_save_file(new File(path + filename));
	}
	
	public static void write_campaign_save_file(String path, String filename, byte[] campaign_marker, int level, String name)
	{
		write_campaign_save_file(new File(path + filename), campaign_marker, level, name);
	}
	
	// returns an array of strings of existing campaign save files from the path
	// if there are none the array shall be null
	public static String[] poll_campaign_save_files(String path)
	{
		try
		{
			File dir = new File(path);
			
			String[] contents = dir.list();
			boolean[] valid = new boolean[contents.length];
			
			for (int i = 0; i < contents.length; i++)
			{
				String str = contents[i];
				
				// check its a .SAV file
				valid[i] = check_file_type(str, "SAV");
			}
			
			int sum = 0;
			for (int i = 0; i < valid.length; i++)
			{
				if (valid[i])
					sum++;
			}
			
			if (sum == 0)
				return null;
			
			String[] valid_files = new String[sum];
			
			int count = 0;
			for (int i = 0; i < valid.length; i++)
			{
				if (valid[i])
				{
					valid_files[count] = contents[i];
					count++;
				}
			}
			
			return valid_files;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// returns an array of strings of campaign files, i.e. the markers from path
	// none means array will be none
	public static String[] poll_campaign_files(String path)
	{
		try
		{
			File dir = new File(path);
			
			return dir.list();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// validates thats the current campaign marker exists in the path
	// returns 0 if it exists,
	// -1 if not.
	public static int validate_campaign_file(String path, String marker)
	{
		try
		{
			File dir = new File(path);
			
			String[] contents = dir.list();
			
			for (int i = 0; i < contents.length; i++)
			{
				if (marker.equals(contents[i]))
					return 0;
			}
			
			return -1;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	// gets the biggest level present in the campaign marker folder
	// highest number
	// -1 if anything goes wrong
	public static int get_campaign_max_level(String path, String marker)
	{
		int max_lev = -1;
		try
		{
			File dir = new File(path + marker);
			
			String[] contents = dir.list();
			
			for (int i = 0; i < contents.length; i++)
			{
				// check its a valid caveman level
				if (check_file_type(contents[i], "CVL"))
				{
					// split from dot
					// escape the dot!!!
					String[] split = contents[i].split("\\.");
					
					// extract integer from lev 
					int level = Integer.parseInt(split[0].substring(3, split[0].length()));
					
					if (level > max_lev)
					{
						max_lev = level;
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		return max_lev;
	}
	
	// check if this file type is the same as the arg
	// returns true if so,
	// false otherwise
	public static boolean check_file_type(String name, String type)
	{
		int name_len = name.length();
		int type_len = type.length();
		
		// +1 for the dot
		if (name_len > (type_len + 1))
			if (name.substring(name_len - type_len - 1, name_len).equals(('.' + type)))
				return true;
			
		return false;
	}
}