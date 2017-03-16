package dataAccess;
import static java.util.Arrays.asList;

import java.net.SocketTimeoutException;
//import java.rmi.RemoteException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;
import javax.persistence.PersistenceException;

import com.objectdb.o.UserException;

public final class ExponentialBackOff {

	private static final int[] FIBONACCI = new int[] {1, 1, 2, 3, 5, 8, 13};

	private static final List<Class<? extends Exception>> EXPECTED_COMMUNICATION_ERRORS = asList(SSLHandshakeException.class, SocketTimeoutException.class, PersistenceException.class, IllegalStateException.class, UserException.class);

	private ExponentialBackOff() {
	}

	public static <T> T execute(ExponentialBackOffInterface<T> fn) {
		for (int attempt = 0; attempt < FIBONACCI.length; attempt++) {
			try {
				return fn.execute();
			} catch (Exception e) {
				System.err.println(e.getMessage() + "\nRetrying operation.");
				handleFailure(attempt, e);
			}
		}
		throw new RuntimeException( "Failed to communicate." );
	}

	private static void handleFailure(int attempt, Exception e) {
		if (e.getCause() != null && !EXPECTED_COMMUNICATION_ERRORS.contains( e.getCause().getClass() )) {
			throw new RuntimeException( e );
		}
		doWait( attempt );
	}

	private static void doWait(int attempt) {
		try {
			Thread.sleep( FIBONACCI[attempt] * 1000 );
		} catch (InterruptedException e) {
			throw new RuntimeException( e );
		}
	}
}
