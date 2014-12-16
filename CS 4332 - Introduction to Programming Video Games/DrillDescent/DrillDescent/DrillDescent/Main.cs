using MonoMac.Foundation;
using MonoMac.AppKit;

namespace DrillDescent
{
    /// <summary>
    /// Main class.
    /// </summary>
    class MainClass {
        /// <summary>
        /// The entry point of the program, where the program control starts and ends.
        /// </summary>
        /// <param name='args'>
        /// The command-line arguments.
        /// </param>
        static void Main (string [] args) {
            NSApplication.Init ();

            using (var p = new NSAutoreleasePool ()) {
                NSApplication.SharedApplication.Delegate = new AppDelegate ();
                NSApplication.Main (args);
            }
        }
    }

    /// <summary>
    /// App delegate.
    /// </summary>
    [MonoMac.Foundation.Register("AppDelegate")]
    public class AppDelegate : NSApplicationDelegate {
        /// <summary>
        /// The game.
        /// </summary>
        private MacGame game;

        /// <summary>
        /// Called when Mac app is finished launching.
        /// </summary>
        /// <param name='notification'>
        /// Notification.
        /// </param>
        public override void FinishedLaunching (NSObject notification) {
            game = new MacGame();
            game.Run();
        }

        /// <summary>
        /// Called when the last open window is closed to determine whether or not
        /// the applcation should then terminate.
        /// </summary>
        public override bool ApplicationShouldTerminateAfterLastWindowClosed (NSApplication sender) {
            return true;
        }
    }
}