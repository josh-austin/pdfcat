package org.bjup.pdfcat

import groovy.util.logging.Slf4j
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.util.PDFMergerUtility

@Slf4j
class PdfSplicer {

    boolean duplexMode = false
    String destination
    ArrayList<String> sources

    static main(String[] args) {
        def app = new PdfSplicer()
        def cmdSources = new ArrayList<String>()
        for (int i=0; i<args.length; ++i) {
            if (i == args.length - 1) {
                // the last command line argument is the destination PDF
                app.setDestination(args[i])
            } else if (args[i] == '-d') {
                // the '-d' tag activates duplex mode
                app.setDuplexMode(true)
            } else {
                // a source PDF
                cmdSources.add(args[i])
            }
        }
        app.setSources(cmdSources)
    }

    public PdfSplicer(ArrayList<String> sources = null, String destination = null) {
        this.sources = sources
        this.destination = destination
    }

    public void splice() {
        PDFMergerUtility merger = new PDFMergerUtility();
        for (String source in this.sources) {
            log.info 'getting source file ' + source
            merger.addSource source
        }
        merger.setDestinationFileName(destination)
        merger.mergeDocuments()
        log.info 'PDFs merged into destination ' + destination
        if (this.duplexMode) {
            // duplex mode is when a blank page needs to be added to the end of a
            // destination PDF with odd-numbered pages (for printing reasons)
            PDDocument doc = PDDocument.load destination
            if (doc.getNumberOfPages() % 2 != 0) {
                PDPage blankPage = new PDPage()
                doc.addPage blankPage
                doc.save destination
                log.info 'added blank page to ' + destination
            }
            doc.close()
        }
    }
}
