package de.mpicbg.ulman.imgstreamer;

import java.io.IOException;
import java.io.OutputStream;

public class ZeroMQOutputStream extends OutputStream
{
	// -------------- buffering stuff --------------
	//private final byte[] buf = new byte[1<<20]; //= 1 MB
	private final byte[] buf = new byte[50000];
	private int pos = 0;

	/** Writes one (1) byte into the underlying buffer, which is possibly
	 *  flushed away into the underlying ZMQ communication.
	 *
	 * @throws IOException If ZMQ.send() will have some trouble.
	 */
	@Override
	public void write(int b)
	throws IOException
	{
		if (pos == buf.length)
		{
			zmq.writeZMQ(buf,pos);
			pos = 0;
		}

		buf[pos++] = (byte)b;
	}

	@Override
	public void flush()
	throws IOException
	{
		zmq.writeZMQ(buf,pos);
	}

	/** request to close the stream */
	@Override
	public void close()
	{
		zmq.close();
	}

	// -------------- ZMQ stuff --------------
	private final ZeroMQsession zmq;

	/** inits this OutputStream by binding to a local port */
	public
	ZeroMQOutputStream(final int portNo)
	throws IOException
	{
		zmq = new ZeroMQsession(portNo);
	}

	/** inits this OutputStream by binding to a local port */
	public
	ZeroMQOutputStream(final int portNo, final int timeOut)
	throws IOException
	{
		zmq = new ZeroMQsession(portNo,timeOut);
	}

	/** inits this OutputStream by connecting to given URL */
	public
	ZeroMQOutputStream(final String URL)
	throws IOException
	{
		zmq = new ZeroMQsession(URL);
	}

	/** inits this OutputStream by connecting to given URL */
	public
	ZeroMQOutputStream(final String URL, final int timeOut)
	throws IOException
	{
		zmq = new ZeroMQsession(URL,timeOut);
	}
}
