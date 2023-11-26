package com.zeus.utils.DictionaryUtil;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Word class contains 1 word target, 1 description contains
 * pronoun and all type, meaning, examples.
 */
public class Word {
    private String wordTarget;
    private Description description;

    /**
     * Default constructor.
     */
    public Word() {
    }

    /**
     * Constructor.
     *
     * @param wordTarget  Word target.
     * @param description Word's {@link Description}
     */
    public Word(String wordTarget, Description description) {
        this.wordTarget = wordTarget;
        this.description = description;
    }

    /**
     * Set word target and description.
     *
     * @param wordTarget  Word target.
     * @param description Word's {@link Description}
     */
    @JsonAnySetter
    public void setWord(String wordTarget, Description description) {
        this.wordTarget = wordTarget;
        this.description = description;
    }

    /**
     * Get word target.
     *
     * @return Word target.
     */
    public String getWordTarget() {
        return wordTarget;
    }

    /**
     * Set word target.
     *
     * @param wordTarget Word target.
     */
    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    /**
     * Get word's pronoun.
     *
     * @return Word's pronoun obtain from {@link Description#getPronoun()}.
     */
    public String getPronoun() {
        return description.getPronoun();
    }

    /**
     * Get word's {@link Description}.
     *
     * @return Word's {@link Description}.
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Set word's {@link Description}.
     *
     * @param description {@link Description}.
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * To string method, include {@link Description#toString()} method.
     *
     * @return The string represent the word information.
     */
    @Override
    public String toString() {
        return String.format("\n\"%s\":{", wordTarget) +
                description.toString() +
                "}";
    }

    /**
     * Similar to {@link Word#toString()} but with
     * some modification to print out the console.
     *
     * @return The string after processed to print to console.
     */
    public String print() {
        return String.format("\"%s\": {", wordTarget) +
                description.print().replaceAll("(?m)^", "\t") +
                "},\n";
    }

    /**
     * Description class.
     */
    public static class Description {
        private String pronoun;
        private List<Type> types;

        /**
         * Set description properties.
         *
         * @param pronoun Pronoun.
         * @param types   {@link Type} list.
         */
        @JsonAnySetter
        public void setDescription(String pronoun, List<Type> types) {
            this.pronoun = pronoun;
            this.types = types;
        }

        /**
         * To string method, include {@link Type#toString()} method.
         *
         * @return The string represent the description information.
         */
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append(String.format("\"pronoun\":\"%s\",", pronoun));
            result.append("\"type\":[");
            if (types.size() > 1) {
                for (Type t : types) {
                    result.append(t.toString()).append(",");
                }
                result.deleteCharAt(result.length() - 1);
            } else if (types.size() == 1) {
                result.append(types.get(0).toString());
            }
            result.append("]");
            return result.toString();
        }

        /**
         * Same purpose as {@link Word} and {@link #toString()}.
         *
         * @return String to print.
         */
        public String print() {
            StringBuilder result = new StringBuilder();
            result.append(String.format("\n\"pronoun\": %s\n", pronoun));
            result.append("\"type\": [\n");
            for (Type t : types) {
                result.append(t.print().replaceAll("(?m)^", "\t"));
            }
            result.append("]\n");
            return result.toString();
        }

        /**
         * Get pronoun.
         *
         * @return Pronoun.
         */
        public String getPronoun() {
            return pronoun;
        }

        /**
         * Set pronoun.
         *
         * @param pronoun Pronoun.
         */
        @JsonProperty("pronoun")
        public void setPronoun(String pronoun) {
            this.pronoun = pronoun;
        }

        /**
         * Get {@link Type} list.
         *
         * @return {@link Type} list.
         */
        public List<Type> getTypes() {
            return types;
        }

        /**
         * Set {@link Type} list.
         *
         * @param types {@link Type} list.
         */
        @JsonProperty("type")
        public void setTypes(List<Type> types) {
            this.types = types;
        }

        /**
         * Class Type of Word.
         */
        public static class Type {
            private String name;
            private List<Meaning> meanings;

            /**
             * Set the type.
             *
             * @param name     Type's name.
             * @param meanings Type's {@link Meaning} list.
             */
            @JsonAnySetter
            public void setType(String name, List<Meaning> meanings) {
                this.name = name;
                this.meanings = meanings;
            }

            /**
             * G
             *
             * @return Type's name.
             */
            public String getName() {
                return name;
            }

            /**
             * Set type's name.
             *
             * @param name Type's name.
             */
            public void setName(String name) {
                this.name = name;
            }

            /**
             * Get {@link Meaning} list.
             *
             * @return {@link Meaning} list.
             */
            public List<Meaning> getMeanings() {
                return meanings;
            }

            /**
             * Set {@link Meaning} list.
             *
             * @param meanings {@link Meaning} list.
             */
            public void setMeanings(List<Meaning> meanings) {
                this.meanings = meanings;
            }

            /**
             * To string method, include {@link Meaning#toString()}.
             *
             * @return String represent Type's information.
             */
            @Override
            public String toString() {
                StringBuilder result = new StringBuilder();
                result.append(String.format("{\"%s\":[", name));
                if (meanings.size() > 1) {
                    for (Meaning m : meanings) {
                        result.append(m.toString()).append(",");
                    }
                    result.deleteCharAt(result.length() - 1);
                } else if (meanings.size() == 1) {
                    result.append(meanings.get(0).toString());
                }

                result.append("]}");
                return result.toString();
            }

            /**
             * See {@link Word#print()} and {@link #toString()}.
             *
             * @return String to print.
             */
            public String print() {
                StringBuilder result = new StringBuilder();
                result.append(String.format("\"%s\": [\n", name));
                for (Meaning m : meanings) {
                    result.append(m.print().replaceAll("(?m)^", "\t"));
                }
                result.append("],\n");
                return result.toString();
            }

            /**
             * Meaning class of Word.
             */
            public static class Meaning {
                private String explain;
                private List<Example> examples;

                /**
                 * Set meaning.
                 *
                 * @param explain  Explain in Vietnamese.
                 * @param examples {@link Example} list.
                 */
                @JsonAnySetter
                public void setMeaning(String explain, List<Example> examples) {
                    this.explain = explain;
                    this.examples = examples;
                }

                /**
                 * Get explain.
                 *
                 * @return Explain.
                 */
                public String getExplain() {
                    return explain;
                }

                /**
                 * Get {@link Example} list.
                 *
                 * @return {@link Example} list.
                 */
                public List<Example> getExamples() {
                    return examples;
                }

                /**
                 * To string method, include {@link Example#toString()}.
                 *
                 * @return String represent Meaning's information.
                 */
                @Override
                public String toString() {
                    StringBuilder result = new StringBuilder();
                    result.append(String.format("{\"%s\":[", explain));
                    if (examples.size() > 1) {
                        for (Example e : examples) {
                            result.append(e.toString()).append(",");
                        }
                        result.deleteCharAt(result.length() - 1);
                    } else if (examples.size() == 1) {
                        result.append(examples.get(0).toString());
                    }
                    result.append("]}");
                    return result.toString();
                }

                /**
                 * See {@link Word#print()} and {@link #toString()}.
                 *
                 * @return String to print.
                 */
                public String print() {
                    StringBuilder result = new StringBuilder();
                    result.append(String.format("\"%s\": [", explain));
                    if (!examples.isEmpty()) result.append('\n');
                    for (Example e : examples) {
                        result.append(e.print().replaceAll("(?m)^", "\t"));
                    }
                    result.append("],\n");
                    return result.toString();
                }

                /**
                 * Example class.
                 */
                public static class Example {
                    private String english;
                    private String vietnamese;

                    /**
                     * Set example.
                     *
                     * @param english    Example in english.
                     * @param vietnamese Example in Vietnamese.
                     */
                    @JsonAnySetter
                    public void setExample(String english, String vietnamese) {
                        this.english = english;
                        this.vietnamese = vietnamese;
                    }

                    /**
                     * Get English example.
                     *
                     * @return English example.
                     */
                    public String getEnglish() {
                        return english;
                    }

                    /**
                     * Get Vietnamese example.
                     *
                     * @return Vietnamese example.
                     */
                    public String getVietnamese() {
                        return vietnamese;
                    }

                    /**
                     * To string method.
                     *
                     * @return String represent example.
                     */
                    @Override
                    public String toString() {
                        return String.format("{\"%s\":\"%s\"}", english, vietnamese);
                    }

                    /**
                     * Get string to print.
                     *
                     * @return String to print.
                     */
                    public String print() {
                        return String.format("\"%s\": \"%s\"\n", english, vietnamese);
                    }
                }
            }
        }
    }
}
