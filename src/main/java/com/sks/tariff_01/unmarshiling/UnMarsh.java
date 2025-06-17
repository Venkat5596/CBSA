package com.sks.tariff_01.unmarshiling;

import com.sks.tariff_01.model.GstCodeDetail;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Component
@Primary
public class UnMarsh {

    public GstCodeDetail unmarshalGstDetails(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(
                new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8))
        );

        XPath xpath = XPathFactory.newInstance().newXPath();
        Node propertiesNode = (Node) xpath.evaluate("//*[local-name()='properties']", doc, XPathConstants.NODE);

        JAXBContext context = JAXBContext.newInstance(GstCodeDetail.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (GstCodeDetail) unmarshaller.unmarshal(new DOMSource(propertiesNode));
    }
}