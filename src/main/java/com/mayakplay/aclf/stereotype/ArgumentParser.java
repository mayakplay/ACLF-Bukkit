package com.mayakplay.aclf.stereotype;

import com.mayakplay.aclf.exception.ACLFException;

/**
 *
 *
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
public interface ArgumentParser {

    Object getParsedObject() throws ACLFException;

}