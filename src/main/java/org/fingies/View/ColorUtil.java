package org.fingies.View;

import java.awt.Color;
import java.util.Objects;
import java.util.Random;

public class ColorUtil {
	
	/** The lightest color allowed in light mode. */
	private static Color lightModeBrightestColor = new Color(240, 240, 240);
	/** The darkest color allowed in light mode. */
	private static Color lightModeDarkestColor = new Color(100, 100, 100);

	/** The lightest color allowed in dark mode. */
	private static Color darkModeBrightestColor = new Color(140, 140, 140);
	/** The darkest color allowed in dark mode. */
	private static Color darkModeDarkestColor = new Color(15, 15, 15);

	
	private static String[] webColorNames = {"aliceblue", "antiquewhite", "aqua", "aquamarine", "azure", "beige", "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown", "burlywood", "cadetblue", "chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan", "darkblue", "darkcyan", "darkgoldenrod", "darkgray", "darkgrey", "darkgreen", "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon", "darkseagreen", "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise", "darkviolet", "deeppink", "deepskyblue", "dimgray", "dimgrey", "dodgerblue", "firebrick", "floralwhite", "forestgreen", "fuchsia", "gainsboro", "ghostwhite", "gold", "goldenrod", "gray", "grey", "green", "greenyellow", "honeydew", "hotpink", "indianred", "indigo", "ivory", "khaki", "lavender", "lavenderblush", "lawngreen", "lemonchiffon", "lightblue", "lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgray", "lightgrey", "lightgreen", "lightpink", "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray", "lightslategrey", "lightsteelblue", "lightyellow", "lime", "limegreen", "linen", "magenta", "maroon", "mediumaquamarine", "mediumblue", "mediumorchid", "mediumpurple", "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise", "mediumvioletred", "midnightblue", "mintcream", "mistyrose", "moccasin", "navajowhite", "navy", "oldlace", "olive", "olivedrab", "orange", "orangered", "orchid", "palegoldenrod", "palegreen", "paleturquoise", "palevioletred", "papayawhip", "peachpuff", "peru", "pink", "plum", "powderblue", "purple", "rebeccapurple", "red", "rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell", "sienna", "silver", "skyblue", "slateblue", "slategray", "slategrey", "snow", "springgreen", "steelblue", "tan", "teal", "thistle", "tomato", "turquoise", "violet", "wheat", "white", "whitesmoke", "yellow", "yellowgreen"};
	private static int[] webColorHexes = {0xF0F8FF, 0xFAEBD7, 0x00FFFF, 0x7FFFD4, 0xF0FFFF, 0xF5F5DC, 0xFFE4C4, 0x000000, 0xFFEBCD, 0x0000FF, 0x8A2BE2, 0xA52A2A, 0xDEB887, 0x5F9EA0, 0x7FFF00, 0xD2691E, 0xFF7F50, 0x6495ED, 0xFFF8DC, 0xDC143C, 0x00FFFF, 0x00008B, 0x008B8B, 0xB8860B, 0xA9A9A9, 0xA9A9A9, 0x006400, 0xBDB76B, 0x8B008B, 0x556B2F, 0xFF8C00, 0x9932CC, 0x8B0000, 0xE9967A, 0x8FBC8F, 0x483D8B, 0x2F4F4F, 0x2F4F4F, 0x00CED1, 0x9400D3, 0xFF1493, 0x00BFFF, 0x696969, 0x696969, 0x1E90FF, 0xB22222, 0xFFFAF0, 0x228B22, 0xFF00FF, 0xDCDCDC, 0xF8F8FF, 0xFFD700, 0xDAA520, 0x808080, 0x808080, 0x008000, 0xADFF2F, 0xF0FFF0, 0xFF69B4, 0xCD5C5C, 0x4B0082, 0xFFFFF0, 0xF0E68C, 0xE6E6FA, 0xFFF0F5, 0x7CFC00, 0xFFFACD, 0xADD8E6, 0xF08080, 0xE0FFFF, 0xFAFAD2, 0xD3D3D3, 0xD3D3D3, 0x90EE90, 0xFFB6C1, 0xFFA07A, 0x20B2AA, 0x87CEFA, 0x778899, 0x778899, 0xB0C4DE, 0xFFFFE0, 0x00FF00, 0x32CD32, 0xFAF0E6, 0xFF00FF, 0x800000, 0x66CDAA, 0x0000CD, 0xBA55D3, 0x9370DB, 0x3CB371, 0x7B68EE, 0x00FA9A, 0x48D1CC, 0xC71585, 0x191970, 0xF5FFFA, 0xFFE4E1, 0xFFE4B5, 0xFFDEAD, 0x000080, 0xFDF5E6, 0x808000, 0x6B8E23, 0xFFA500, 0xFF4500, 0xDA70D6, 0xEEE8AA, 0x98FB98, 0xAFEEEE, 0xDB7093, 0xFFEFD5, 0xFFDAB9, 0xCD853F, 0xFFC0CB, 0xDDA0DD, 0xB0E0E6, 0x800080, 0x663399, 0xFF0000, 0xBC8F8F, 0x4169E1, 0x8B4513, 0xFA8072, 0xF4A460, 0x2E8B57, 0xFFF5EE, 0xA0522D, 0xC0C0C0, 0x87CEEB, 0x6A5ACD, 0x708090, 0x708090, 0xFFFAFA, 0x00FF7F, 0x4682B4, 0xD2B48C, 0x008080, 0xD8BFD8, 0xFF6347, 0x40E0D0, 0xEE82EE, 0xF5DEB3, 0xFFFFFF, 0xF5F5F5, 0xFFFF00, 0x9ACD32};
	
	private static String[] minecraftMobNames = {"allay", "armadillo", "axolotl", "bat", "camel", "cat", "chicken", "cod", "cow", "donkey", "frog", "glowsquid", "horse", "mooshroom", "mule", "ocelot", "parrot", "pig", "pufferfish", "rabbit", "salmon", "sheep", "skeletonhorse", "sniffer", "snowgolem", "squid", "strider", "tadpole", "tropicalfish", "turtle", "villager", "wanderingtrader", "bee", "cavespider", "dolphin", "drowned", "enderman", "fox", "goat", "irongolem", "llama", "panda", "piglin", "polarbear", "spider", "traderllama", "wolf", "zombifiedpiglin", "blaze", "bogged", "breeze", "creaking", "creeper", "elderguardian", "endermite", "evoker", "ghast", "guardian", "hoglin", "husk", "magmacube", "phantom", "piglinbrute", "pillager", "ravager", "shulker", "silverfish", "skeleton", "slime", "stray", "vex", "vindicator", "warden", "witch", "witherskeleton", "zoglin", "zombie", "zombievillager", "enderdragon", "wither", "chickenjockey", "spiderjockey", "steve", "alex", "herobrine", "notch"};
	private static int[] minecraftMobHexes = {0x3de0f7, 0x9e625e, 0xfbc0e3, 0x322822, 0xf5ba5d, 0x1c1827, 0xc6c6c6, 0x92715a, 0x443626, 0x7a6a59, 0xe58757, 0x309b9c, 0x6f4515, 0xa80f13, 0x442718, 0xf2d19a, 0xea0001, 0xee9a95, 0xd79519, 0xa08b73, 0x923d39, 0xdedede, 0xcbc9c9, 0x802117, 0xffffff, 0x546d80, 0xae3e3e, 0x6d533d, 0xb67d1d, 0x3fa442, 0xbe886c, 0x35517e, 0xecc343, 0x394439, 0xb0c3d9, 0x4d9280, 0x161616, 0xe17c20, 0xe8e5db, 0xd0d0d0, 0xf5e2b8, 0xa1a1a3, 0xe7a074, 0xf6f6f6, 0x605348, 0xfcecc6, 0xdedadb, 0xc97d68, 0xffff84, 0x67763a, 0x978ed3, 0x6d6361, 0x6dc964, 0xaeab97, 0x4c1d39, 0x919696, 0xf0f0f0, 0x62857b, 0xe49d75, 0xc9ae75, 0x992907, 0x4f61a4, 0xe7a074, 0x959b9b, 0x534944, 0x976797, 0x868686, 0xd3d3d3, 0x75c164, 0x576c6d, 0x95acc3, 0x8e9393, 0x0e1319, 0x2c2814, 0x313131, 0xa37f58, 0x769a62, 0x769a62, 0x1d1d1d, 0x505353, 0xc6c6c6, 0x394439, 0x13baba, 0x7eb675, 0x13baba, 0xb28474};
	
	/**
	 * Creates a random color based off a given string.
	 * Certain strings generate very specific colors, just for fun ;)
	 * @param str The string to get a color for.
	 * @return A color based on the string provided.
	 */
	public static Color colorOfString(String str)
	{
		return colorOfString(str, false);
	}
	
	/**
	 * Creates a random color based off a given string. Darker colors are given if darkMode is true.
	 * Certain strings generate very specific colors, just for fun ;)
	 * @param str The string to get a color for.
	 * @param darkMode Whether the color should be for dark mode or not.
	 * @return A color based on the string provided.
	 */
	public static Color colorOfString(String str, boolean darkMode)
	{
		Color brightestColor, darkestColor;
		if (darkMode)
		{
			brightestColor = darkModeBrightestColor;
			darkestColor = darkModeDarkestColor;
		}
		else
		{
			brightestColor = lightModeBrightestColor;
			darkestColor = lightModeDarkestColor;
		}
		
		str = removeUnderscores(str.toLowerCase());
		switch(str)
		{
			case "red":
			case "r":
				return clamp(Color.red, brightestColor, darkestColor);
			case "green":
			case "g":
				return clamp(Color.green, brightestColor, darkestColor);
			case "blue":
			case "b":
				return clamp(Color.blue, brightestColor, darkestColor);
			case "yellow":
				return clamp(Color.yellow, brightestColor, darkestColor);
			case "cyan":
				return clamp(Color.cyan, brightestColor, darkestColor);
			case "magenta":
				return clamp(Color.magenta, brightestColor, darkestColor);
			case "orange":
				return clamp(Color.orange, brightestColor, darkestColor);
			case "pink":
				return clamp(Color.pink, brightestColor, darkestColor);
			case "black":
				return darkestColor;
			case "white":
				return brightestColor;
			case "gray":
			case "grey":
				return average(darkestColor, brightestColor);
			case "lightgray":
			case "lightgrey":
				return average(brightestColor, average(brightestColor, darkestColor));
			case "darkgray":
			case "darkgrey":
				return average(darkestColor, average(darkestColor, brightestColor));
			default:
				// check if it's a web color
				for (int i = 0; i < webColorNames.length; ++i)
					if (webColorNames[i].equals(str))
							return clamp(new Color(webColorHexes[i]), brightestColor, darkestColor);
				
				// otherwise, check if it's a minecraft mob
				for (int i = 0; i < minecraftMobNames.length; ++i)
					if (minecraftMobNames[i].equals(str))
							return clamp(new Color(minecraftMobHexes[i]), brightestColor, darkestColor);
				
				// otherwise, generate a random color
				int hash = Objects.hash(str);
		        Random random = new Random(hash);

		        // Generate a color clamped to the appropriate range
		        int r = random.nextInt(brightestColor.getRed() - darkestColor.getRed()) + darkestColor.getRed();
		        int g = random.nextInt(brightestColor.getGreen() - darkestColor.getGreen()) + darkestColor.getGreen();
		        int b = random.nextInt(brightestColor.getBlue() - darkestColor.getBlue()) + darkestColor.getBlue();

		        return new Color(r, g, b);
		}
	}
	
	/**
	 * Clamps the given color between two other colors. Ensures that the resulting color's rgb values each individually fall between
	 * the rgb values of the brighterBound and darkerBound.
	 * @param color The color to clamp
	 * @param brighterBound A color representing the maximum allowed rgb values
	 * @param darkerBound A color representing the minimum allowed rgb values
	 * @return A color with its rgb values clamped to be between brighterBound and darkerBound
	 */
	public static Color clamp(Color color, Color brighterBound, Color darkerBound) {
	    int red = Math.min(brighterBound.getRed(), Math.max(darkerBound.getRed(), color.getRed()));
	    int green = Math.min(brighterBound.getGreen(), Math.max(darkerBound.getGreen(), color.getGreen()));
	    int blue = Math.min(brighterBound.getBlue(), Math.max(darkerBound.getBlue(), color.getBlue()));
	    return new Color(red, green, blue);
	}

	/**
	 * Averages the rgb values of two colors together.
	 * @param color1 The first color to average.
	 * @param color2 The second color to average.
	 * @return A color whose rgb values are an average of the two given colors'.
	 */
	public static Color average(Color color1, Color color2)
	{
		return new Color ((color1.getRed() + color2.getRed()) / 2, (color1.getGreen() + color2.getGreen()) / 2, (color1.getBlue() + color2.getBlue()) / 2);
	}
	
	/**
	 * REmoves all of the underscores from a string.
	 * @param str The string to remove the underscores from.
	 * @return The original string but without any underscores.
	 */
	public static String removeUnderscores(String str)
	{
		return String.join("", str.split("_"));
	}

}
