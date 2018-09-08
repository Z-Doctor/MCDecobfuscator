package zdoctor.mcdeobfuscator.modes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import zdoctor.commons.utils.data.BinaryReader;
import zdoctor.commons.utils.data.NodeDictionary;
import zdoctor.commons.utils.data.NodeDictionary.Node;
import zdoctor.mcdeobfuscator.Manager;

public class Deobfuscator {
	public static byte[] deobfuscate(NodeDictionary<String> dic, byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BinaryReader br = new BinaryReader(data);
		ByteArrayOutputStream byteData = new ByteArrayOutputStream();

		Node<String> currentNode = dic.getNodeTree();

		while (br.available() > 0) {
			if (currentNode.getValue() == null) {
				byte key = (byte) br.read();
				byteData.write(key);
				Node<String> node = new Node<>(key);
				if (currentNode.hasChild(node))
					currentNode = currentNode.getChild(node);
				else {
					bos.write(byteData.toByteArray());
					byteData = new ByteArrayOutputStream();
					currentNode = dic.getNodeTree();
				}
			} else {
				byte[] block = currentNode.getValue().getBytes();
				Manager.logReplacement(new String(currentNode.getKey()), currentNode.getValue());

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
