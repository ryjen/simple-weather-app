import SwiftUI

extension Color {
    static let primaryBlue = Color(red: 0x00 / 255, green: 0x61 / 255, blue: 0xA4 / 255)
    static let secondaryOrange = Color(red: 0x9C / 255, green: 0x43 / 255, blue: 0x00 / 255)
    static let tertiaryYellow = Color(red: 0x71 / 255, green: 0x5D / 255, blue: 0x00 / 255)

    static let backgroundLight = Color(red: 0xFC / 255, green: 0xFC / 255, blue: 0xFF / 255)
}

extension Color {
    init(hex: UInt, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}
