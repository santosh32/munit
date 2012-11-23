package org.mule.munit;

import org.apache.sshd.common.keyprovider.AbstractKeyPairProvider;
import org.apache.sshd.common.util.SecurityUtils;
import org.bouncycastle.openssl.PEMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Reads the resource from the jar</p>
 *
 * @author Federico, Fernando
 */
public class StreamKeyPairProvider extends AbstractKeyPairProvider {

    private static final Logger LOG = LoggerFactory.getLogger(StreamKeyPairProvider.class);


    @Override
    protected KeyPair[] loadKeys() {
            if (!SecurityUtils.isBouncyCastleRegistered()) {
                throw new IllegalStateException("BouncyCastle must be registered as a JCE provider");
            }
            List<KeyPair> keys = new ArrayList<KeyPair>();
                try {
                    PEMReader r = new PEMReader(new InputStreamReader(getClass().getResourceAsStream("/ftp-hostkey.pem")), null);
                    try {
                        Object o = r.readObject();
                        if (o instanceof KeyPair) {
                            keys.add((KeyPair) o);
                        }
                    } finally {
                        r.close();
                    }
                } catch (Exception e) {
                    LOG.info("Unable to read key ", e);
                }

            return keys.toArray(new KeyPair[keys.size()]);
    }
}
