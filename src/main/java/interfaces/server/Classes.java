package interfaces.server;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * User: Alex
 * Date: 28/11/12
 * Time: 12:54
 */
public class Classes {

	public static Set<Method> listAllAnnotatedMethods(Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation){
		Set<Method> methods = new HashSet<Method>();
		for (Class clazz : Classes.listAnnotatedClasses(classAnnotation)) {
			methods.addAll(Classes.listAnnotatedMethods(clazz, methodAnnotation));
		}
		return methods;
	}

	public static Set<Method> listAnnotatedMethods(Class clazz, Class<? extends Annotation> annotation) {
		Set<Method> methods = new HashSet<Method>();
		for (Method m : clazz.getMethods()) {
			if (m.getAnnotation(annotation) != null){
				methods.add(m);
			}
		}
		return methods;
	}

	public static Set<Class> listAnnotatedClasses(Class<? extends Annotation> annotation) {
		Set<Class> classes = new HashSet<Class>();
		try {
			Enumeration<URL> e = ClassLoader.getSystemClassLoader().getResources("");
			while (e.hasMoreElements()) {
				URL url = e.nextElement();
				for (String child : Classes.getChildren(url)) {
					if (child.endsWith(".class")) {
						String className = child.substring(0, child.length() - 6).replace("/", ".");
						Class<?> clazz = Class.forName(className);

						if (clazz.getAnnotation(annotation) != null) {
							classes.add(Class.forName(clazz.getName()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return classes;
	}


	/**
	 * Takes a given url and creates a list which contains
	 * all children of the given url.
	 * (Works with Files and JARs).
	 */
	public static List<String> getChildren(URL url) {
		List<String> result = new ArrayList<String>();
		if ("file".equals(url.getProtocol())) {
			File file = new File(url.getPath());
			if (!file.isDirectory()) {
				file = file.getParentFile();
			}
			addFiles(file, result, file);
		} else if ("jar".equals(url.getProtocol())) {
			try {
				JarFile jar = ((JarURLConnection)
						url.openConnection()).getJarFile();
				Enumeration<JarEntry> e = jar.entries();
				while (e.hasMoreElements()) {
					JarEntry entry = e.nextElement();
					result.add(entry.getName());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static void addFiles(File file,
								 List<String> result,
								 File reference) {
		if (!file.exists() || !file.isDirectory()) {
			return;
		}
		for (File child : file.listFiles()) {
			if (child.isDirectory()) {
				addFiles(child, result, reference);
			} else {
				String path = null;
				while (child != null && !child.equals(reference)) {
					if (path != null) {
						path = child.getName() + "/" + path;
					} else {
						path = child.getName();
					}
					child = child.getParentFile();
				}
				result.add(path);
			}
		}
	}
}
