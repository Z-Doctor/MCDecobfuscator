package zdoctor.mcdeobfuscator.modes;

public class ObfDeobfuscator {
//	public static byte[] deobfuscate(StringDictironary dic, byte[] data) throws IOException {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		BinaryReader br = new BinaryReader(data);
//		ByteArrayOutputStream byteData = new ByteArrayOutputStream();
//
//		Node<Character, String> currentNode = dic.getNodeTree();
//
//		while (br.available() > 0) {
//			String line = br.readLine();
//			String newLine = "";
//			
//			System.out.println("Before: " + line);
//			BinaryReader lineReader = new BinaryReader(line.getBytes());
//			while (lineReader.available() > 0) {
//				int pos = (int) lineReader.position();
//				String search = lineReader.readString('/', '$', '_', '\'');
//				if (search.length() > 0 && dic.hasKey(search)) {
////						System.out.println(search + ":" + dic.lookUp(search));
//						String result = dic.lookUp(search);
//						newLine += result;
//				} else
//					newLine += line.substring(pos, (int) lineReader.position());
//			}
//			lineReader.close();
//			System.out.println("After: " + newLine);
//		}
//		return data;
//			if (currentNode.getValue() == null) {
//				if (isValidStart((char) br.peekBack())) {
//					Character key = (char) br.read();
//					byteData.write(key);
//					if (currentNode.hasChild(key))
//						currentNode = currentNode.getChild(key);
//					else {
//						bos.write(byteData.toByteArray());
//						byteData = new ByteArrayOutputStream();
//						currentNode = dic.getNodeTree();
//					}
//				} else {
////					bos.write(br.read());
//					br.read();
//					bos.write('x');
//				}
//			} else if (!isEnd((char) br.peek())) {
//				Character key = (char) br.peek();
//				if (currentNode.hasChild(key)) {
//					br.read();
//					currentNode = currentNode.getChild(key);
//					byteData.write(key);
//				} else {
//					byte[] block = currentNode.getValue().getBytes();
//					Manager.logReplacement(PrimitiveUtil.toString(currentNode.getKey()), currentNode.getValue());
//
//					bos.write(block);
//					byteData = new ByteArrayOutputStream();
//					currentNode = dic.getNodeTree();
//				}
//			} else {
//				byte[] block = currentNode.getValue().getBytes();
//				Manager.logReplacement(PrimitiveUtil.toString(currentNode.getKey()), currentNode.getValue());
//
//				bos.write(block);
//				byteData = new ByteArrayOutputStream();
//				currentNode = dic.getNodeTree();
//			}
//		}
//		br.close();
//		bos.close();
//		return bos.toByteArray();
//	}

//	public static boolean isValidStart(char prevChar) {
//		if (Character.isLetterOrDigit(prevChar))
//			return false;
////		if (prevChar == '.')
////			return true;
//
//		return true;
//	}

//	public static boolean isEnd(char nextChar) {
//		if (Character.isLetterOrDigit(nextChar))
//			return false;
//
//		if (nextChar == '$' || nextChar == '/')
//			return false;
//
//		return true;
//	}
}
