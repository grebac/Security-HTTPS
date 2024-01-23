package hepl.grebac.hepl_security.TLS;

import org.springframework.stereotype.Component;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

@Component
public class requestToACQ {
    public requestToACQ() {
        System.setProperty("javax.net.ssl.keyStore", "https.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "heplPass");

        System.setProperty("javax.net.ssl.trustStore", "acq.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "heplPass");
    }

    public boolean requestToACS(String token) throws IOException {
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocketForACS = (SSLSocket) sslsocketfactory.createSocket("localhost", 7777);

        var writer = GetBufferedWriter(sslSocketForACS);
        var reader = GetBufferedReader(sslSocketForACS);

        writer.write(token + "\n");
        writer.flush();

        var answer = reader.readLine();

        if(answer.equals("ACK")) {
            System.out.println("Token is valid");
            return true;
        }
        else {
            System.out.println("Token is invalid");
            return false;
        }
    }

    private BufferedWriter GetBufferedWriter(SSLSocket sslsocket) throws IOException {
        OutputStream outputstream = sslsocket.getOutputStream();
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream));
        return bufferedwriter;
    }

    private BufferedReader GetBufferedReader(SSLSocket sslsocket) throws IOException {
        InputStream inputstream = sslsocket.getInputStream();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        return bufferedreader;
    }
}
