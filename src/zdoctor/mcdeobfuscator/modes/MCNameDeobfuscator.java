package zdoctor.mcdeobfuscator.modes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import zdoctor.commons.utils.PrimitiveUtil;
import zdoctor.commons.utils.data.BinaryReader;
import zdoctor.commons.utils.data.NodeDictionary.Node;
import zdoctor.commons.utils.data.StringDictironary;
import zdoctor.mcdeobfuscator.Manager;

public class MCNameDeobfuscator {
	public static byte[] deobfuscate(StringDictironary dic, byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BinaryReader br = new BinaryReader(data);
		ByteArrayOutputStream byteData = new ByteArrayOutputStream();

		Node<Character, String> currentNode = dic.getNodeTree();

		while (br.available() > 0) {
			if (currentNode.getValue() == null) {
				Character key = (char) br.read();
				byteData.write(key);
				if (currentNode.hasChild(key))
					currentNode = currentNode.getChild(key);
				else {
					bos.write(byteData.toByteArray());
					byteData = new ByteArrayOutputStream();
					currentNode = dic.getNodeTree();
				}
			} else {
				byte[] block = currentNode.getValue().getBytes();
				Manager.logReplacement(PrimitiveUtil.toString(currentNode.getKey()), currentNode.getValue());

				bos.write(block);
				byteData = new ByteArrayOutputStream();
				currentNode = dic.getNodeTree();
			}
		}
		br.close();
		bos.close();
		return bos.toByteArray();
	}
}
