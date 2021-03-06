package tv.matchstick.client.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tv.matchstick.client.internal.ValueChecker;

public class MyStringBuilder {

	public static boolean compare(Object a, Object b) {
		return ((a == b) || ((a != null) && (a.equals(b))));
	}

	public static int hashCode(Object[] objects) {
		return Arrays.hashCode(objects);
	}

	public static StringBuilder_a newStringBuilder(Object root) {
		return new StringBuilder_a(root);
	}

	public static final class StringBuilder_a {
		private final List<String> Ce;
		private final Object Cf;

		private StringBuilder_a(Object root) {
			this.Cf = ValueChecker.checkNullPointer(root);
			this.Ce = new ArrayList<String>();
		}

		public StringBuilder_a append(String key, Object value) {
			this.Ce.add(ValueChecker.checkNullPointer(key) + "="
					+ String.valueOf(value));
			return this;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(100).append(
					this.Cf.getClass().getSimpleName()).append('{');
			int i = this.Ce.size();
			for (int j = 0; j < i; ++j) {
				sb.append((String) this.Ce.get(j));
				if (j >= i - 1) {
					continue;
				}
				sb.append(", ");
			}
			return sb.append('}').toString();
		}
	}

}
