package cn.maiaimei.example;

import cn.maiaimei.example.util.MailHelper;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;
import lombok.SneakyThrows;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.search.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.function.Consumer;

public class JavaEmailTest {
    private static final String USERNAME = "1211674185@qq.com";
    private static final String IMAP_PASSWORD = "[imap_password]";
    private static final String POP3_PASSWORD = "[pop3_password]";

    public static void main(String[] args) {
        //listFolders();
        //receiveEmailByImap();
        receiveEmailByPop3();
    }

    /**
     * 使用POP3协议接收邮件
     */
    private static void receiveEmailByPop3() {
        connectMailBoxByPop3(store -> {
            try {
                // 获取收件箱
                Folder folder = store.getFolder("INBOX");
                // 以读写方式打开
                folder.open(Folder.READ_WRITE);
                Message[] messages = getAllMessages(folder);
                for (Message message : messages) {
                    MailHelper.printMessage((MimeMessage) message);
                }
                // 关闭资源
                folder.close(Boolean.FALSE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * 使用IMAP协议接收邮件
     */
    private static void receiveEmailByImap() {
        connectMailBoxByImap(store -> {
            try {
                // 获取收件箱
                Folder folder = store.getFolder("INBOX");
                // 以读写方式打开，READ_WRITE模式读取完成后邮件变成已读状态
                folder.open(Folder.READ_WRITE);
                Message[] messages = getUnreadMessages(folder);
                System.out.println("未读邮件：" + folder.getUnreadMessageCount());
                for (Message message : messages) {
                    String subject = MailHelper.getSubject(message);
                    if (message.getFlags().contains(Flags.Flag.SEEN)) {
                        continue;
                    }
                    System.out.println("[" + subject + "]未读，是否需要阅读此邮件（yes/no）？");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String answer = reader.readLine();
                    if ("yes".equalsIgnoreCase(answer)) {
                        MailHelper.printMessage((MimeMessage) message);
                        // 设置已读标志，第二个参数如果设置为true，则将修改反馈给服务器。false则不反馈给服务器
                        message.setFlag(Flags.Flag.SEEN, true);
                    }
                }
                // 关闭资源
                folder.close(Boolean.FALSE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @SneakyThrows
    private static void connectMailBoxByPop3(Consumer<Store> consumer) {
        String host = "pop.qq.com";
        int port = 995;

        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.port", String.valueOf(port));
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

        // 创建Session实例对象
        Session session = Session.getInstance(props, null);
        // 是否显示调试信息
        session.setDebug(false);
        // 创建POP3协议的Store对象，并连接邮件服务器
        URLName url = new URLName("pop3", host, port, "", USERNAME, POP3_PASSWORD);
        Store store = new POP3SSLStore(session, url);
        store.connect();

        consumer.accept(store);

        // 关闭资源
        store.close();
    }

    @SneakyThrows
    private static void connectMailBoxByImap(Consumer<Store> consumer) {
        String host = "imap.qq.com";
        int port = 993;

        // 准备连接服务器的会话信息
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", String.valueOf(port));
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.port", String.valueOf(port));

        // 创建Session实例对象
        Session session = Session.getInstance(props, null);
        // 是否显示调试信息
        session.setDebug(false);
        // 创建IMAP协议的Store对象，并连接邮件服务器
        URLName url = new URLName("imap", host, port, "", USERNAME, IMAP_PASSWORD);
        Store store = new IMAPSSLStore(session, url);
        store.connect();

        consumer.accept(store);

        // 关闭资源
        store.close();
    }

    /**
     * 列出邮箱文件夹
     */
    private static void listFolders() {
        connectMailBoxByImap(store -> {
            System.out.println("The mailbox has the following folders: ");
            try {
                Folder[] folders = store.getDefaultFolder().list();
                for (Folder folder : folders) {
                    System.out.println(folder.getFullName() + ": ");
                    if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0) {
                        // TODO: 如何读取自定义文件夹邮件？
                    }
                    if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                        // 打印邮件总数/新邮件数量/未读数量/已删除数量
                        System.out.println("\tTotal messages: " + folder.getMessageCount());
                        System.out.println("\tNew messages: " + folder.getNewMessageCount());
                        System.out.println("\tUnread messages: " + folder.getUnreadMessageCount());
                        System.out.println("\tDeleted messages: " + folder.getDeletedMessageCount());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * 获取所有邮件
     */
    private static Message[] getAllMessages(Folder folder) throws Exception {
        return folder.getMessages();
    }

    /**
     * 获取未读邮件
     */
    private static Message[] getUnreadMessages(Folder folder) throws Exception {
        return folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
    }

    /**
     * 按条件获取邮件
     */
    private static Message[] getMessagesByConditions(Folder folder) throws Exception {
        // 搜索发件人为"maiaimei"，而且邮件正文包含“test“的所有邮件
        //SearchTerm searchTerm = new AndTerm(new FromStringTerm("maiaimei"), new BodyTerm("test"));

        // 搜索发件人为“maiaimei“或主题包含“test“的所有邮件
        SearchTerm searchTerm = new OrTerm(new FromStringTerm("1114962998"), new SubjectTerm("测试"));

        return folder.search(searchTerm);
    }
}
