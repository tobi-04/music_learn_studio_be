package com.tobi.MusicLearn_Studio_Backend.common.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class SlugService {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern DASHES = Pattern.compile("[-]+");

    /**
     * Generate URL-friendly slug from text
     * Example: "Khóa học Guitar Cơ Bản" -> "khoa-hoc-guitar-co-ban"
     */
    public String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String slug = input.trim().toLowerCase(Locale.ENGLISH);

        // Normalize Vietnamese characters
        slug = normalizeVietnamese(slug);

        // Normalize Unicode
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);

        // Replace whitespace with dashes
        slug = WHITESPACE.matcher(slug).replaceAll("-");

        // Remove non-latin characters except dashes
        slug = NON_LATIN.matcher(slug).replaceAll("");

        // Remove consecutive dashes
        slug = DASHES.matcher(slug).replaceAll("-");

        // Remove leading/trailing dashes
        slug = slug.replaceAll("^-+|-+$", "");

        return slug;
    }

    /**
     * Generate unique slug by appending counter if needed
     */
    public String generateUniqueSlug(String baseSlug, java.util.function.Predicate<String> existsCheck) {
        String slug = baseSlug;
        int counter = 1;

        while (existsCheck.test(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    /**
     * Normalize Vietnamese characters to ASCII
     */
    private String normalizeVietnamese(String text) {
        text = text.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        text = text.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        text = text.replaceAll("[ìíịỉĩ]", "i");
        text = text.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        text = text.replaceAll("[ùúụủũưừứựửữ]", "u");
        text = text.replaceAll("[ỳýỵỷỹ]", "y");
        text = text.replaceAll("đ", "d");

        return text;
    }
}
