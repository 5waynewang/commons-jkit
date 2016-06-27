package org.until.keyword.dictionary;

import org.until.keyword.criterion.CharStandardization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;


/**
 * @author mega
 */
public class DartsDictionaryStandardization {

	public static void main(String[] args) {
		if (args == null)
			return;
		try {
			Pattern p = Pattern.compile("\\\\u[0-9abcdefABCDEF]{4}");
			for (String filename : args) {
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
				String phrase;
				Set<String> phrases = new TreeSet<String>();
				Set<String> unicodePhrases = new TreeSet<String>();
				Set<String> ignorePhrases = new TreeSet<String>();
				while ((phrase = in.readLine()) != null) {
					phrase = CharStandardization.compositeTextConvert(phrase, false, true, true, false, false, true,
							false);
					if (phrase.length() == 0)
						continue;
					if (phrase.startsWith("#")) {
						ignorePhrases.add(phrase.trim());
					} else if (p.matcher(phrase).matches()) {
						unicodePhrases.add(phrase);
					} else {
						phrases.add(phrase.trim());
					}
				}
				in.close();
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF8"));
				for (String s : unicodePhrases) {
					out.write(s);
					out.write("\r\n");
				}
				for (String s : phrases) {
					out.write(s);
					out.write("\r\n");
				}
				for (String s : ignorePhrases) {
					out.write(s);
					out.write("\r\n");
				}
				out.flush();
				out.close();
				System.out.println(filename + " dictionary standardize successful");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
