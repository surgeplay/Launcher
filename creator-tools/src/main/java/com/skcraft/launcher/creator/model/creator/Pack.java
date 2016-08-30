/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.creator.model.creator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.io.BaseEncoding;
import com.skcraft.launcher.builder.BuilderConfig;
import com.skcraft.launcher.builder.BuilderOptions;
import com.skcraft.launcher.creator.Creator;
import com.skcraft.launcher.persistence.Persistence;
import lombok.Data;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;

@Data
public class Pack {

	private String location;
	@JsonIgnore private Workspace workspace;
	@JsonIgnore private BuilderConfig cachedConfig;
	@JsonIgnore private Image icon;

	@JsonIgnore
	public File getDirectory() {
		File path = new File(location);
		if (path.isAbsolute()) {
			return path;
		} else {
			return new File(workspace.getDirectory(), location);
		}
	}

	@JsonIgnore
	public File getLoadersDir() {
		return new File(getDirectory(), "loaders");
	}

	@JsonIgnore
	public File getSourceDir() {
		return new File(getDirectory(), "src");
	}

	@JsonIgnore
	public File getModsDir() {
		return new File(getSourceDir(), "mods");
	}

	@JsonIgnore
	public File getConfigFile() {
		return new File(getDirectory(), BuilderOptions.DEFAULT_CONFIG_FILENAME);
	}

	public void load() {
		setCachedConfig(Persistence.read(getConfigFile(), BuilderConfig.class, true));
		getLoadersDir().mkdirs();
		getSourceDir().mkdirs();
		try {
			BufferedImage img = ImageIO.read(new File(getSourceDir(), "pack-icon.png"));
			BufferedImage iconImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Image scaled = img.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
			MediaTracker mt = new MediaTracker(Box.createGlue());
			mt.addImage(scaled, 0);
			mt.waitForAll();
			Graphics2D g2d = iconImg.createGraphics();
			g2d.drawImage(scaled, 0, 0, null);
			g2d.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(iconImg, "PNG", baos);
			getCachedConfig().setIcon(BaseEncoding.base64().encode(baos.toByteArray()));
			icon = iconImg;
		} catch (IOException e) {
			try {
				icon = ImageIO.read(Creator.class.getResource("pack_icon.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void createGuideFolders() {
		new File(getSourceDir(), "config").mkdirs();
		new File(getSourceDir(), "mods").mkdirs();
		new File(getSourceDir(), "resourcepacks").mkdirs();
	}

	@JsonIgnore
	public boolean isLoaded() {
		return cachedConfig != null;
	}

}
