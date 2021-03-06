package top.rechinx.meow.core.extension.model

enum class InstallStep {
    Pending, Downloading, Installing, Installed, Error;

    fun isCompleted(): Boolean {
        return this == Installed || this == Error
    }
}
