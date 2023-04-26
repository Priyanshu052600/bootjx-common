package com.boot.jx.types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.boot.utils.FileUtil;

public class Pnum extends Dnum<Pnum> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Pnum.class);

	protected Pnum(String name, int ordinal) {
		super(name, ordinal);
	}

	public static <E> Dnum<? extends Dnum<?>>[] values() {
		return values(Pnum.class);
	}

	public static <E> void init(Class<E> clazz) {
		try {
			initProps(clazz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static <E> InputStream readStream(Class<E> clazz) throws IOException {

		String propertyFile = clazz.getSimpleName() + ".properties";
		InputStream inSideInputStream = null;

		inSideInputStream = FileUtil.getExternalResourceAsStream(propertyFile, clazz);
		if (inSideInputStream != null) {
			LOGGER.info("Loaded Properties from jarpath: {}", propertyFile);
			return inSideInputStream;
		}

		URL ufile = FileUtil.getResource(propertyFile, clazz);
		if (ufile != null) {
			inSideInputStream = ufile.openStream();
			LOGGER.info("Loaded Properties from classpath: {}", ufile.getPath());
			if (inSideInputStream != null) {
				return inSideInputStream;
			}
		}

		return inSideInputStream;
	}

	private static <E> void initProps(Class<E> clazz) throws Exception {
		// String rcName = clazz.getName().replace('.', '/') + ".properties";
		// BufferedReader reader = new BufferedReader(
		// new
		// InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(rcName)));
		InputStream is = null;
		try {
			is = readStream(clazz);
			if (is == null) {
				return;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			Constructor<E> minimalConstructor = getConstructor(clazz, new Class[] { String.class, int.class });
			Constructor<E> additionalConstructor1 = getConstructor(clazz,
					new Class[] { String.class, int.class, String.class });
			Constructor<E> additionalConstructor2 = getConstructor(clazz,
					new Class[] { String.class, int.class, String.class, String.class });
			Constructor<E> additionalConstructor3 = getConstructor(clazz,
					new Class[] { String.class, int.class, String.class, String.class, String.class });
			Constructor<E> additionalConstructor4 = getConstructor(clazz,
					new Class[] { String.class, int.class, String.class, String.class, String.class, String.class });
			Constructor<E> additionalConstructor5 = getConstructor(clazz, new Class[] { String.class, int.class,
					String.class, String.class, String.class, String.class, String.class });

			int ordinal = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				line = line.replaceFirst("#.*", "").trim();
				if (line.equals("")) {
					continue;
				}
				String[] parts = line.split("\\s*=\\s*");
				if (parts.length == 1) {
					minimalConstructor.newInstance(parts[0], ordinal++);
				} else {
					String[] args = parts[1].split("\\s*,\\s*");
					if (args.length == 1) {
						additionalConstructor1.newInstance(parts[0], ordinal++, args[0]);
					} else if (args.length == 2 && additionalConstructor2 != null) {
						additionalConstructor2.newInstance(parts[0], ordinal++, args[0], args[1]);
					} else if (args.length == 3 && additionalConstructor3 != null) {
						additionalConstructor3.newInstance(parts[0], ordinal++, args[0], args[1], args[2]);
					} else if (args.length == 4 && additionalConstructor4 != null) {
						additionalConstructor4.newInstance(parts[0], ordinal++, args[0], args[1], args[2], args[3]);
					} else if (args.length == 5 && additionalConstructor5 != null) {
						additionalConstructor5.newInstance(parts[0], ordinal++, args[0], args[1], args[2], args[3],
								args[4]);
					} else if (minimalConstructor != null) {
						minimalConstructor.newInstance(parts[0], ordinal++);
					}
				}
			}

		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("readPropertyException", e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void readEnums() {

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(Pnum.class));

		Set<BeanDefinition> components = provider.findCandidateComponents("com/amx");
		for (BeanDefinition component : components) {
			try {
				Class cls = Class.forName(component.getBeanClassName());
				init(cls);
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
				LOGGER.error("No Default Constructor {}(Pnum apiError)", component.getBeanClassName(), e);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static <E> Constructor<E> getConstructor(Class<E> clazz, Class<?>[] argTypes) {
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			try {
				if (argTypes.length == 2) {
					return (Constructor<E>) c.getDeclaredConstructor(String.class, int.class);
				} else if (argTypes.length == 3) {
					return (Constructor<E>) c.getDeclaredConstructor(String.class, int.class, String.class);
				} else if (argTypes.length == 4) {
					return (Constructor<E>) c.getDeclaredConstructor(argTypes[0], argTypes[1], argTypes[2],
							argTypes[3]);
				} else if (argTypes.length == 5) {
					return (Constructor<E>) c.getDeclaredConstructor(argTypes[0], argTypes[1], argTypes[2], argTypes[3],
							argTypes[4]);
				} else if (argTypes.length == 6) {
					return (Constructor<E>) c.getDeclaredConstructor(argTypes[0], argTypes[1], argTypes[2], argTypes[3],
							argTypes[4], argTypes[5]);
				} else if (argTypes.length == 7) {
					return (Constructor<E>) c.getDeclaredConstructor(argTypes[0], argTypes[1], argTypes[2], argTypes[3],
							argTypes[4], argTypes[5], argTypes[6]);
				} else if (argTypes.length == 8) {
					return (Constructor<E>) c.getDeclaredConstructor(argTypes[0], argTypes[1], argTypes[2], argTypes[3],
							argTypes[4], argTypes[5], argTypes[6], argTypes[7]);
				} else {
					return (Constructor<E>) c.getDeclaredConstructor(String.class, int.class);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}

}
