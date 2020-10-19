package SerialIzation;

import java.io.Serializable;

public class TypeOfRequestParseXML implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum typesParserXML {
		DOM, SAX, STAX
	}
	private typesParserXML typeParserXML;

	public typesParserXML getTypeParserXML() {
		return typeParserXML;
	}

	public TypeOfRequestParseXML(typesParserXML typeParserXML) {
		this.typeParserXML = typeParserXML;
	}
}
