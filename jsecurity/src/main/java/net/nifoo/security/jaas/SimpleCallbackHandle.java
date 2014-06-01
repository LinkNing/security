package net.nifoo.security.jaas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class SimpleCallbackHandle implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

		for (Callback callback : callbacks) {

			if (callback instanceof NameCallback) {
				NameCallback nc = (NameCallback) callback;

				System.out.print(nc.getPrompt());
				System.out.flush();

				nc.setName((new BufferedReader(new InputStreamReader(System.in))).readLine());
				
			} else if (callback instanceof PasswordCallback) {

				PasswordCallback pcb = (PasswordCallback) callback;

				System.out.print(pcb.getPrompt());
				System.out.flush();
				pcb.setPassword((new BufferedReader(new InputStreamReader(System.in))).readLine().toCharArray());
			}
		}
	}
}
