package com.github.lessjava.exceptions;
/**
 * Error encountered during static/semantic analysis.
 */
public class InvalidProgramException extends Exception
{
    public static final long serialVersionUID = 1L;

    public InvalidProgramException(String msg)
    {
        super(msg);
    }
}
