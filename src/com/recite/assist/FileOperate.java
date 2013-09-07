package com.recite.assist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.recite.entity.Word;
import com.recite.sql.AllUnitNameOperate;
import com.recite.sql.ImportedBookOperate;

public class FileOperate
{
	public static File[] mergeDirectoryFile_internals = new File[6],
			           mergeDirectoryFile_externals = new File[6],
	                   splitDirectoryFiles = new File[6];
	public static AllUnitNameOperate  allUnitNameOperate= null;
	
	public Context context;
	
	public FileOperate(Context context)
	{
		init();
		makedir();
		allUnitNameOperate = new AllUnitNameOperate(context);
		this.context = context;
	}
	
	private void  init()
	{
		for(int i=0;i<6;++i)
		{
			mergeDirectoryFile_externals[i] = new File(Values.mergeFilePaths_EXTERNAL[i]);
			mergeDirectoryFile_internals[i] = new File(Values.mergeFilePaths_INTERNAL[i]);
			splitDirectoryFiles[i] = new File(Values.splitLexiconPaths[i]);
		}
	}
	
	private void makedir()
	{
		for(int i=0;i<6;++i)
		{
			File splitDirectoryFile = splitDirectoryFiles[i];
			if(!splitDirectoryFile.exists())
			{
				splitDirectoryFile.mkdirs();
			}
			else if(splitDirectoryFile.exists()&& !splitDirectoryFile.isDirectory())
			{
				splitDirectoryFile.delete();
				splitDirectoryFile.mkdirs();
			}	
		}	
	}
	
	public static FileOperate getInstance(Context context)
	{
		return  new FileOperate(context);
	}
	
	public boolean importFile() throws Exception
	{
		if(!isExternalStorageWritable())
		{
			throw new Exception("内存卡异常");
		}
		boolean haveDownload = false;
		for(int i=0;i<6;++i)
		{
			haveDownload = splitAll(i);
		}
		return haveDownload;
	}
	
	private boolean splitAll(int grade) throws Exception
	{
		if(!mergeDirectoryFile_externals[grade].exists()&&!mergeDirectoryFile_internals[grade].exists())
		{
			return false;
		}
		
		
		File[] mergeFiles_external = mergeDirectoryFile_externals[grade].listFiles(new SuffixFileFilter(Values.mergeFilePostfixString));
		File[] mergeFiles_internal = mergeDirectoryFile_internals[grade].listFiles(new SuffixFileFilter(Values.mergeFilePostfixString));
		
		
		extract(mergeFiles_external,grade);
		extract(mergeFiles_internal,grade);
		
		if( (mergeFiles_external!=null&&mergeFiles_external.length>0)
			||(mergeFiles_internal!=null&&mergeFiles_internal.length>0) )
			return true;
		return false;
		
	}
	
	private boolean isExtracted(String bookName,List<String> bookNames)
	{
		for(int i=0;i<bookNames.size();++i)
		{
			if(bookNames.get(i).equals(bookName))
				return true;
		}
		return false;
	}
	
	private void extract(File[] mergeFiles,int grade)
	{
		if(mergeFiles == null) return ;
		
		
		List<String> needExtractedBookNames = new ArrayList<String>();
		
		List<String> extractedBookNames = ImportedBookOperate.getInstance(context).query(grade);
		for(int i=0;i<mergeFiles.length;++i)
		{
			
			String bookName = Tool.removeSuffix(mergeFiles[i].getName());
			
			if(!isExtracted(bookName, extractedBookNames))
			{
			split(mergeFiles[i], grade);
			needExtractedBookNames.add(bookName);
			}
		}
		ImportedBookOperate.getInstance(context).insert(needExtractedBookNames, grade);
	}
	
	private void split(File mergeFile,int grade)
	{
		String bookName = Tool.removeSuffix(mergeFile.getName());
		try
		{
			RandomAccessFile read = new RandomAccessFile(mergeFile, "r");
			File enFile = new File(Values.splitLexiconPaths[grade]+"/"+bookName+".em"), 
					 chFile = new File(Values.splitLexiconPaths[grade]+"/"+bookName+".cm"),
					 txtFile = new File(Values.splitLexiconPaths[grade]+"/"+bookName+".t"),
					 idFile = new File(Values.splitLexiconPaths[grade]+"/"+bookName+".id");
			
			if(enFile.exists())
			{
				enFile.delete();
			}
			enFile.createNewFile();
	
			if(chFile.exists())
			{
				chFile.delete();
			}
			chFile.createNewFile();
			
			if(txtFile.exists())
			{
				txtFile.delete();
			}
			txtFile.createNewFile();
			
			if(idFile.exists())
			{
				idFile.delete();
			}
			idFile.createNewFile();
			
			RandomAccessFile enMp3Raf = new RandomAccessFile(enFile, "rw"),
						     chMp3Raf = new RandomAccessFile(chFile, "rw"),
					         txtRaf = new RandomAccessFile(txtFile, "rw"),
		                     idRaf = new RandomAccessFile(idFile, "rw");	
			byte[] b = new byte[12];
			read.read(b);
			int[] pos = Tool.byteArrayToint(b);
			writeInto(read, enMp3Raf,pos[0]);
			writeInto(read, chMp3Raf,pos[1]);
			writeInto(read, txtRaf,pos[2]);
			writeInto(read,idRaf,(int)(mergeFile.length()-read.getFilePointer()) );
			read.close();
			enMp3Raf.close();
			chMp3Raf.close();
			txtRaf.close();
			idRaf.close();
			importUnitList(idFile,bookName,grade);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void writeInto(RandomAccessFile from,RandomAccessFile to,int l)
	{
		try
		{
			byte[] b = new byte[8192];
			int len;
			while(l>0)
			{
				if(l>8192)
				{
					len = from.read(b);
					l-=len;
					to.write(b,0,len);
				}
				else
				{
					from.read(b, 0, l);
					to.write(b, 0, l);
					l = 0;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	
//	public boolean importFirstData()
//	{
//		for (int grade = 0; grade < 6; ++grade)
//		{
//			
//			File splitDirectoryFile = splitDirectoryFiles[grade];
//			
//			File[] files = splitDirectoryFile.listFiles(new SuffixFileFilter(
//					".id"));
//			if (files == null || files.length <= 0)
//				continue;
//			for (int i = 0; i < files.length; ++i)
//			{
//				String bookname = files[i].getName();
//				bookname = Tool.removeSuffix(bookname);
//				importUnitList(files[i], bookname,grade);
//			}
//		}
//		return true;
//	}
	
	
	public void importUnitList(File file,String bookName,int grade)
	{
		List<String> allUnitname = new ArrayList<String>();
		BufferedReader bufferedReader = null;
		try
		{
			InputStreamReader inputStreamReader = new InputStreamReader(
					new FileInputStream(file), "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String s = null;
			while((s = bufferedReader.readLine())!=null)
			{
				if(s.length() == 0) continue;
				s = bookName + "\r\n" + s + "\r\n" + String.valueOf(allUnitname.size());
				allUnitname.add(s);
			}
			allUnitNameOperate.insert(allUnitname,grade);
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				bufferedReader.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	public static boolean isExternalStorageWritable() 
	{
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state))
	    {
	        return true;
	    }
	    return false;
	}
	
	public List<Word> readWords(String bookName,int unitIndex,int grade) throws Exception
	{
		List<Word> words = new ArrayList<Word>();
		RandomAccessFile rafTxt = null;
		File[] files = splitDirectoryFiles[grade].listFiles(new FileNameFileFilter(bookName + ".t"));
		if(files.length <= 0)
		{
			throw new Exception("文件损坏");
		}
		File fileTxt = files[0];
		try
		{
			rafTxt = new RandomAccessFile(fileTxt, "r");
			rafTxt.seek(10);
			int len = rafTxt.readInt();
			byte[] b = new byte[len];
			rafTxt.read(b);
			int[] unitPosition = Tool.byteArrayToint(b);
			int start = unitPosition[unitIndex];
			int unitLen;
			if(unitIndex == unitPosition.length - 1) 
			{
				unitLen = (int)rafTxt.length() - start;
			}
			else 
			{
				unitLen = (int)unitPosition[unitIndex+1] - start;
			}
			byte[] buffer = new byte[unitLen];
			rafTxt.seek(start);
			rafTxt.read(buffer);
			String s = new String(buffer, "UTF-8");
			String temp[] = s.split("\r\n");

			for(int i=1;i<temp.length - 1;i+=4)
			{
				Word word = new Word(temp[i],temp[i+1],temp[i+2],temp[i+3]);
				word.bookName = bookName;
				words.add(word);
			}			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				rafTxt.close();
			} catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}		
		return words;
	}
	
	public File createTempMp3(Word word,int i,int grade) throws Exception
	{
		String bookName = word.bookName;
		File[] files = null;
		File tempFile = null,file = null;
		int start = 0,len = 0;
		switch(i)
		{
		case 1:
			start = word.enStart;
			len = word.enlen;
			files = splitDirectoryFiles[grade].listFiles(new FileNameFileFilter(bookName + ".em"));
			try
			{
				tempFile = File.createTempFile(word.name+"com", ".tmp1");
				tempFile.deleteOnExit();
			}catch (Exception e)
			{
				throw e;
			}
			break;
		case 2:
			start = word.chStart;
			len = word.chlen;
			files = splitDirectoryFiles[grade].listFiles(new FileNameFileFilter(bookName + ".cm"));
			try
			{
				tempFile = File.createTempFile(word.name+"com", ".tmp2");
				tempFile.deleteOnExit();
			}catch (Exception e)
			{
				throw e;
			}
			break;
		}
		if(files.length<= 0)
		{
			throw new Exception("词库文件损坏");
		}
		file = files[0];
		try
		{
			RandomAccessFile mp3Raf = new RandomAccessFile(file, "r");
			RandomAccessFile tempRaf = new RandomAccessFile(tempFile, "rw");
			byte[] b = new byte[len];
			mp3Raf.seek(start);
			mp3Raf.read(b);
			tempRaf.write(b);
			mp3Raf.close();
			tempRaf.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		 
		return tempFile;
		
	}
	
}
