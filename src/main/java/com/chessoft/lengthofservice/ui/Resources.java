package com.chessoft.lengthofservice.ui;


import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Resources {
	public static final ImageIcon getImageIcon(String name) {
		Image result;

		try {
			URL url = Resources.class.getResource("/"+name);
			result = Toolkit.getDefaultToolkit().getImage(url);
		} catch (Exception e) {
			System.out.println("Image error! "+e.getMessage());
			e.printStackTrace();
			result = Toolkit.getDefaultToolkit().getImage("/"+name);
		}
		return new ImageIcon(result);
	}
}
