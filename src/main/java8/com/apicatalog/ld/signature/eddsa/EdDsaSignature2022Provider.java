package com.apicatalog.ld.signature.ed25519;

import java.net.URI;
import java.security.GeneralSecurityException;

import com.apicatalog.ld.signature.KeyGenError;
import com.apicatalog.ld.signature.SigningError;
import com.apicatalog.ld.signature.VerificationError;
import com.apicatalog.ld.signature.VerificationError.Code;
import com.apicatalog.ld.signature.algorithm.SignatureAlgorithm;
import com.apicatalog.ld.signature.key.KeyPair;
import com.google.crypto.tink.signature.SignatureConfig;
import com.google.crypto.tink.subtle.Ed25519Sign;
import com.google.crypto.tink.subtle.Ed25519Verify;

public final class EdDsaSignature2022Provider implements SignatureAlgorithm {
    
    @Override
    public void verify(byte[] publicKey, byte[] signature, byte[] data) throws VerificationError {
        try {
            SignatureConfig.register();

            Ed25519Verify verifier = new Ed25519Verify(publicKey);

            verifier.verify(signature, data);

        } catch (GeneralSecurityException e) {
            throw new VerificationError(Code.InvalidSignature, e);
        }
    }

    @Override
    public byte[] sign(byte[] privateKey, byte[] data) throws SigningError {

        try {
            // Register all signature key types with the Tink runtime.
            SignatureConfig.register();

            Ed25519Sign signer = new Ed25519Sign(privateKey);

            byte[] signature = signer.sign(data);

            return signature;

        } catch (GeneralSecurityException e) {
            throw new SigningError(SigningError.Code.Internal, e);
        }
    }

    @Override
    public KeyPair keygen(int length) throws KeyGenError {

        try {
            final Ed25519Sign.KeyPair kp = Ed25519Sign.KeyPair.newKeyPair();

            return new EdDsaKeyPair2022(
                            null,
                            null,
                            URI.create(EdDsaSignature2022.KEY_PAIR_TYPE.uri()),
                            kp.getPublicKey(),
                            kp.getPrivateKey()
                        );

        } catch (GeneralSecurityException e) {
            throw new KeyGenError(e);
        }
    }
}
