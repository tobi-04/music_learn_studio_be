package com.tobi.MusicLearn_Studio_Backend.common.service;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {

    private final PolicyFactory policy;

    public MarkdownService() {
        // Configure allowed HTML tags for sanitization
        this.policy = new HtmlPolicyBuilder()
                .allowElements(
                        "p", "br", "div", "span",
                        "h1", "h2", "h3", "h4", "h5", "h6",
                        "strong", "em", "u", "s", "code", "pre",
                        "ul", "ol", "li",
                        "blockquote",
                        "a", "img",
                        "table", "thead", "tbody", "tr", "th", "td")
                .allowAttributes("href").onElements("a")
                .allowAttributes("src", "alt", "title").onElements("img")
                .allowAttributes("class").globally()
                .requireRelNofollowOnLinks()
                .toFactory();
    }

    /**
     * Sanitize markdown/HTML content to prevent XSS
     */
    public String sanitize(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "";
        }

        return policy.sanitize(content);
    }

    /**
     * Clean markdown content (remove dangerous scripts, tags)
     */
    public String clean(String markdown) {
        if (markdown == null) {
            return "";
        }

        // Remove script tags
        markdown = markdown.replaceAll("(?i)<script[^>]*>.*?</script>", "");

        // Remove style tags
        markdown = markdown.replaceAll("(?i)<style[^>]*>.*?</style>", "");

        // Remove event handlers
        markdown = markdown.replaceAll("(?i)on\\w+\\s*=\\s*[\"'][^\"']*[\"']", "");

        return markdown.trim();
    }

    /**
     * Validate markdown content
     */
    public boolean isValid(String markdown) {
        if (markdown == null) {
            return true;
        }

        // Check for dangerous patterns
        if (markdown.toLowerCase().contains("<script")) {
            return false;
        }

        if (markdown.matches("(?i).*javascript:.*")) {
            return false;
        }

        return true;
    }
}
