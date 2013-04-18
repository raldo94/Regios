package net.jzx7.jnbt;

/*
 * JNBT License
 * 
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the JNBT team nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import net.jzx7.regios.util.RegiosConversions;
import net.jzx7.regiosapi.inventory.RegiosItemStack;

/**
 * <p>
 * This class reads <strong>NBT</strong>, or <strong>Named Binary Tag</strong>
 * streams, and produces an object graph of subclasses of the <code>Tag</code>
 * object.
 * </p>
 * 
 * <p>
 * The NBT format was created by Markus Persson, and the specification may be
 * found at <a href="http://www.minecraft.net/docs/NBT.txt">
 * http://www.minecraft.net/docs/NBT.txt</a>.
 * </p>
 * 
 * @author Graham Edgecombe
 * 
 */
public final class NBTInputStream implements Closeable {

	/**
	 * The data input stream.
	 */
	private final DataInputStream is;

	/**
	 * Creates a new <code>NBTInputStream</code>, which will source its data
	 * from the specified input stream.
	 * 
	 * @param is
	 *            The input stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public NBTInputStream(InputStream is) throws IOException {
		this.is = new DataInputStream(is);
	}

	/**
	 * Reads an NBT tag from the stream.
	 * 
	 * @return The tag that was read.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public Tag readTag() throws IOException {
		return readTag(0);
	}

	/**
	 * Reads an NBT from the stream.
	 * 
	 * @param depth
	 *            The depth of this tag.
	 * @return The tag that was read.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private Tag readTag(int depth) throws IOException {
		int type = is.readByte() & 0xFF;

		String name;
		if (type != NBTConstants.TYPE_END) {
			int nameLength = is.readShort() & 0xFFFF;
			byte[] nameBytes = new byte[nameLength];
			is.readFully(nameBytes);
			name = new String(nameBytes, NBTConstants.CHARSET);
		} else {
			name = "";
		}

		return readTagPayload(type, name, depth);
	}

	/**
	 * Reads the payload of a tag, given the name and type.
	 * 
	 * @param type
	 *            The type.
	 * @param name
	 *            The name.
	 * @param depth
	 *            The depth.
	 * @return The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private Tag readTagPayload(int type, String name, int depth)
			throws IOException {
		switch (type) {
		case NBTConstants.TYPE_END:
			if (depth == 0) {
				throw new IOException(
						"TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
			} else {
				return new EndTag();
			}
		case NBTConstants.TYPE_BYTE:
			return new ByteTag(name, is.readByte());
		case NBTConstants.TYPE_SHORT:
			return new ShortTag(name, is.readShort());
		case NBTConstants.TYPE_INT:
			return new IntTag(name, is.readInt());
		case NBTConstants.TYPE_LONG:
			return new LongTag(name, is.readLong());
		case NBTConstants.TYPE_FLOAT:
			return new FloatTag(name, is.readFloat());
		case NBTConstants.TYPE_DOUBLE:
			return new DoubleTag(name, is.readDouble());
		case NBTConstants.TYPE_BYTE_ARRAY:
			int length = is.readInt();
			byte[] bytes = new byte[length];
			is.readFully(bytes);
			return new ByteArrayTag(name, bytes);
		case NBTConstants.TYPE_STRING:
			length = is.readShort();
			bytes = new byte[length];
			is.readFully(bytes);
			return new StringTag(name, new String(bytes, NBTConstants.CHARSET));
		case NBTConstants.TYPE_LIST:
			int childType = is.readByte();
			length = is.readInt();

			List<Tag> tagList = new ArrayList<Tag>();
			for (int i = 0; i < length; ++i) {
				Tag tag = readTagPayload(childType, "", depth + 1);
				if (tag instanceof EndTag) {
					throw new IOException("TAG_End not permitted in a list.");
				}
				tagList.add(tag);
			}

			return new ListTag(name, NBTUtils.getTypeClass(childType), tagList);
		case NBTConstants.TYPE_COMPOUND:
			Map<String, Tag> tagMap = new HashMap<String, Tag>();
			while (true) {
				Tag tag = readTag(depth + 1);
				if (tag instanceof EndTag) {
					break;
				} else {
					tagMap.put(tag.getName(), tag);
				}
			}

			return new CompoundTag(name, tagMap);
		case NBTConstants.TYPE_INT_ARRAY:
			length = is.readInt();
			int[] ints = new int[length];
			for (int i = 0; i < length; i++) {
				ints[i] = is.readInt();
			}
			return new IntArrayTag(name, ints);
		case NBTConstants.TYPE_LISTSTRING_ARRAY: //Added to load sign text from backup file. -jzx7
			length = is.readInt();
			bytes = new byte[length];
			is.readFully(bytes);
			String rebuilt = new String(bytes, NBTConstants.CHARSET);
			rebuilt = rebuilt.substring(1, (rebuilt.length() - 2));
			List<String> list = Arrays.asList(rebuilt.split("\\],\\["));
			List<String[]> stringlist = new ArrayList<String[]>();
			for(String s : list) {
				if(s.endsWith(",")) {
					s = s.substring(0, (s.length() - 1));
				}
				String[] sa = s.split(",");
				stringlist.add(sa);
			}
			return new ListStringArrayTag(name, stringlist);
		case NBTConstants.TYPE_LISTITEMSTACK_ARRAY: //Added to load container contents from backup file -jzx7
			length = is.readInt();
			bytes = new byte[length];
			is.readFully(bytes);
			String isrebuilt = new String(bytes, NBTConstants.CHARSET);
			isrebuilt = isrebuilt.substring(1, (isrebuilt.length() - 2));
			List<String> itemstackstrlist = Arrays.asList(isrebuilt.split("\\],\\["));
			List<RegiosItemStack[]> itemstacklist = new ArrayList<RegiosItemStack[]>();

			for (String s : itemstackstrlist) {
				int index = 0;
				if(s.endsWith(",")) {
					s = s.substring(0, (s.length() - 1));
				}
				String[] contents = s.split("\\|");
				RegiosItemStack[] isa = new RegiosItemStack[contents.length];
				for(String s1 : contents) {
					if(s1.startsWith("{")) {
						String[] sa = s1.split(",(?![^{}]*})");
						Map<String, Object> map = new HashMap<String,Object>();
						for(String str : sa) {
							String[] strobj = str.split("=(?![^{}]*})");
							if (strobj.length > 1) {
								if(strobj[1].startsWith("{")) {
									strobj[1] = strobj[1].substring(1, strobj[1].length());
									Map<String, Object> encmap = new HashMap<String,Object>();
									String[] enc = strobj[1].split(",");
									for(String encType : enc) {
										String[] encstrobj = encType.split("=");
										if(encstrobj.length > 1) {
											if(encstrobj[1].endsWith("}")) {
												encstrobj[1] = encstrobj[1].substring(0, (encstrobj[1].length() - 2));
											}
											try {
												encmap.put(encstrobj[0].trim(), Integer.parseInt(encstrobj[1].trim()));
											} catch (Exception ex) {
												encmap.put(encstrobj[0].trim(), encstrobj[1].trim());
											}
										}
									}
									map.put(strobj[0].trim(), encmap);
									if(strobj[1].endsWith("}}")){
										isa[index] = RegiosConversions.getRegiosItemStack(ItemStack.deserialize(map));
										index++;
										map.clear();
										encmap.clear();
									}
								} else {
									if(strobj[0].startsWith("{")){
										strobj[0] = strobj[0].substring(1, strobj[0].length());
									}
									if(strobj[1].endsWith("}")) {
										strobj[1] = strobj[1].substring(0, (strobj[1].length() - 1));
										try {
											map.put(strobj[0].trim(), Integer.parseInt(strobj[1].trim()));
										} catch (Exception ex) {
											map.put(strobj[0].trim(), strobj[1].trim());
										}
										isa[index] = RegiosConversions.getRegiosItemStack(ItemStack.deserialize(map));
										index++;
										map.clear();
									} else {
										try {
											map.put(strobj[0].trim(), Integer.parseInt(strobj[1].trim()));
										} catch (Exception ex) {
											map.put(strobj[0].trim(), strobj[1].trim());
										}
									}
								}
							} else if(strobj.length == 1) {
								String[] sa2 = strobj[0].split(",");
								for(String str2 : sa2) {
									String [] singlestrobj = str2.split("=");
									if(singlestrobj.length > 1) {
										if(singlestrobj[0].startsWith("{")){
											singlestrobj[0] = singlestrobj[0].substring(1, singlestrobj[0].length());
										}
										if(singlestrobj[1].endsWith("}")) {
											singlestrobj[1] = singlestrobj[1].substring(0, singlestrobj[1].length() - 1);
											try {
												map.put(singlestrobj[0].trim(), Integer.parseInt(singlestrobj[1].trim()));
											} catch (Exception ex) {
												map.put(singlestrobj[0].trim(), singlestrobj[1].trim());
											}
											isa[index] = RegiosConversions.getRegiosItemStack(ItemStack.deserialize(map));
											index++;
											map.clear();
										} else {
											try {
												map.put(singlestrobj[0].trim(), Integer.parseInt(singlestrobj[1].trim()));
											} catch (Exception ex) {
												map.put(singlestrobj[0].trim(), singlestrobj[1].trim());
											}
										}
									} else {
										isa[index] = null;
										index++;
										map.clear();
									}
								}
							} 
						}
					}
				}
				itemstacklist.add(isa);
			}
			
			return new ListItemStackArrayTag(name, itemstacklist);
		default:
			throw new IOException("Invalid tag type: " + type + ".");
		}
	}

	@Override
	public void close() throws IOException {
		is.close();
	}

}
