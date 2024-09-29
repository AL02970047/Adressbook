import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AddressBook {
    private HashMap<String, String> contacts;

    public AddressBook() {
        contacts = new HashMap<>();
    }

    public void load(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] contact = line.split(",");
                if (contact.length == 2) {
                    contacts.put(contact[0], contact[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar contactos: " + e.getMessage());
        }
    }
    public void save(String fileName) {
        // Crear un backup del archivo antes de guardar
        createBackup(fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, String> entry : contacts.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar contactos: " + e.getMessage());
        }
    }
    public void list() {
        if (contacts.isEmpty()) {
            System.out.println("No hay contactos en la agenda.");
        } else {
            System.out.println("Contactos:");
            for (Map.Entry<String, String> entry : contacts.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }
    public void create(String phoneNumber, String name) {
        if (isValidPhoneNumber(phoneNumber)) {
            if (contacts.containsKey(phoneNumber)) {
                System.out.println("Este número ya está en la agenda.");
            } else {
                contacts.put(phoneNumber, name);
                System.out.println("Contacto agregado.");
            }
        } else {
            System.out.println("Número de teléfono inválido. Debe contener 10 dígitos.");
        }
    }

    public void delete(String phoneNumber) {
        if (contacts.containsKey(phoneNumber)) {
            contacts.remove(phoneNumber);
            System.out.println("Contacto eliminado.");
        } else {
            System.out.println("No se encontró el número en la agenda.");
        }
    }

    public void edit(String phoneNumber, String newName) {
        if (contacts.containsKey(phoneNumber)) {
            contacts.put(phoneNumber, newName);
            System.out.println("Contacto actualizado.");
        } else {
            System.out.println("No se encontró el número en la agenda.");
        }
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    public void createBackup(String fileName) {
        File originalFile = new File(fileName);
        if (originalFile.exists()) {
            File backupFile = new File(fileName + ".bak");
            try (InputStream is = new FileInputStream(originalFile);
                 OutputStream os = new FileOutputStream(backupFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                System.out.println("Backup creado: " + backupFile.getName());
            } catch (IOException e) {
                System.out.println("Error al crear el backup: " + e.getMessage());
            }
        }
    }
    public void menu(String fileName) {
        Scanner scanner = new Scanner(System.in);
        String option;

        do {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Listar contactos");
            System.out.println("2. Crear contacto");
            System.out.println("3. Eliminar contacto");
            System.out.println("4. Editar contacto");
            System.out.println("5. Guardar y salir");
            option = scanner.nextLine();

            switch (option) {
                case "1":
                    list();
                    break;
                case "2":
                    System.out.print("Ingrese el número de teléfono: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Ingrese el nombre del contacto: ");
                    String name = scanner.nextLine();
                    create(phoneNumber, name);
                    break;
                case "3":
                    System.out.print("Ingrese el número de teléfono a eliminar: ");
                    String deletePhone = scanner.nextLine();
                    delete(deletePhone);
                    break;
                case "4":
                    System.out.print("Ingrese el número de teléfono a editar: ");
                    String editPhone = scanner.nextLine();
                    System.out.print("Ingrese el nuevo nombre del contacto: ");
                    String newName = scanner.nextLine();
                    edit(editPhone, newName);
                    break;
                case "5":
                    save(fileName);
                    System.out.println("Agenda guardada. ¡Adiós!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (!option.equals("5"));
    }

    public static void main(String[] args) {
        AddressBook addressBook = new AddressBook();
        String fileName = "contacts.csv";

        addressBook.load(fileName);
        addressBook.menu(fileName);
    }
}