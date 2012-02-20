package com.julu.weibouser.processing;

import com.julu.weibouser.model.User;
import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.template.ListTemplate;
import org.msgpack.template.Template;
import org.msgpack.unpacker.Unpacker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by StarCite.
 * User: andy.song
 * Date: 2/20/12
 * Time: 12:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserStreamUtil {
    
    private static MessagePack messagePack;
    
    static {
        messagePack = new MessagePack();
        messagePack.register(User.class);
    }

    public static byte[] serialization(List<User> needSerUsers) throws IOException {
        Template<User> elementTemplate = messagePack.lookup(com.julu.weibouser.model.User.class);
        Template<List<com.julu.weibouser.model.User>> tmpl = new ListTemplate<User>(elementTemplate);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = messagePack.createPacker(out);
        tmpl.write(packer, needSerUsers);

        return out.toByteArray();
    }
    
    public static List<User> deserialization(byte[] bytes) throws IOException {
        Template<User> elementTemplate = messagePack.lookup(com.julu.weibouser.model.User.class);
        Template<List<com.julu.weibouser.model.User>> tmpl = new ListTemplate<User>(elementTemplate);
        Unpacker unpacker = messagePack.createUnpacker(new ByteArrayInputStream(bytes));
        unpacker.resetReadByteCount();

        return tmpl.read(unpacker, null);
    }
    
}
