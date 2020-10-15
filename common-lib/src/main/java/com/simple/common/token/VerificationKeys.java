package com.simple.common.token;

import java.util.List;

public class VerificationKeys {
    public VerificationKeys() {
    }
    public VerificationKeys(List<VerificationKey> keys) {  this.keys = keys;  }
    private List<VerificationKey> keys;
    public List<VerificationKey> getKeys() {  return keys;  }
    public void setKeys(List<VerificationKey> keys) {  this.keys = keys;  }
}
