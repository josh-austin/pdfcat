package org.bjup.pdfcat

import org.apache.pdfbox.pdmodel.PDDocument
import spock.lang.Specification

class PdfSplicerSpec extends Specification {

    PdfSplicer splicer
    ArrayList<String> sources
    String destination
    PDDocument doc

    def setup() {
        sources = [
                new File(this.class.getClassLoader().getResource('input01.pdf').toURI()).getAbsolutePath(),
                new File(this.class.getClassLoader().getResource('input02.pdf').toURI()).getAbsolutePath()
        ]
        destination = 'testOutput.pdf'
        splicer = new PdfSplicer(sources, destination)
    }

    def cleanup() {
        doc.close()
        new File(destination).delete()
    }

    def "splicing a 1-page PDF and a 2-page PDF results in a 3-page output PDF"() {
        splicer.splice()
        doc = PDDocument.load(destination)
        expect: doc.getNumberOfPages() == 3
    }

    def "splicing a 1-page PDF and a 2-page PDF in duplex mode results in a 4-page output PDF"() {
        splicer.setDuplexMode(true)
        splicer.splice()
        doc = PDDocument.load(destination)
        expect: doc.getNumberOfPages() == 4
    }

    def "splicing two 2-page PDFs in duplex mode results in a 4-page output PDF"() {
        splicer.setSources([
                new File(this.class.getClassLoader().getResource('input02.pdf').toURI()).getAbsolutePath(),
                new File(this.class.getClassLoader().getResource('input02.pdf').toURI()).getAbsolutePath()
        ])
        splicer.setDuplexMode true
        splicer.splice()
        doc = PDDocument.load(destination)
        expect: doc.getNumberOfPages() == 4
    }

}
