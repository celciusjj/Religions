package com.celcius.religions.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BukkitSerializer {

    // BY IRVINGLINK
    public static String toBase64(@NotNull ItemStack[] contents) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutputStream = new BukkitObjectOutputStream(outputStream);

            bukkitOutputStream.writeObject(contents);

            bukkitOutputStream.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ItemStack[] fromBase64ItemStacks(@NotNull String base64) {

        try {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream);

            ItemStack[] itemStacks = (ItemStack[]) bukkitInputStream.readObject();

            bukkitInputStream.close();

            return itemStacks;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
