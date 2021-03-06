/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Smooth Velocity Tele-Op", group="Linear Opmode")  // @Autonomous(...) is the other common choice
public class TeleOp_SmoothVelocity extends LinearOpMode {

    final double rotationsUpOneLevel = 0.5;
    final double rotationsDownOneLevel = -rotationsUpOneLevel;
    int targetliftPosition = 1;
    int currentLiftPosition = 1;

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor = null;
    DcMotor rightMotor = null;

    DcMotor intakeMotor = null;
    DcMotor liftMotor = null;

    double leftSpeed = 0;
    double rightSpeed = 0;

    double multiplier = 0.5;

    double increasingVelocityDiff = 0.02;
    double decreasingVelocityDiff = 0.05;

    boolean turning = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        // rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            /**
             * Driving
             */

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            if (gamepad1.left_stick_y < 0 && gamepad1.right_stick_y < 0) { // forwards
                leftSpeed += increasingVelocityDiff;
                rightSpeed += increasingVelocityDiff;
                turning = false;
            } else if (gamepad1.left_stick_y > 0 && gamepad1.right_stick_y > 0) { // backwards
                leftSpeed -= increasingVelocityDiff;
                rightSpeed -= increasingVelocityDiff;
                turning = false;
            } else if (gamepad1.left_stick_y > 0 && gamepad1.right_stick_y < 0) { // turn left
                leftSpeed -= increasingVelocityDiff;
                rightSpeed += increasingVelocityDiff;
                turning = true;
            } else if (gamepad1.left_stick_y < 0 && gamepad1.right_stick_y > 0) { // turn right
                leftSpeed += increasingVelocityDiff;
                rightSpeed -= increasingVelocityDiff;
                turning = true;
            } else {
                if (leftSpeed < 0) leftSpeed += decreasingVelocityDiff;
                else leftSpeed -= decreasingVelocityDiff;

                if (rightSpeed < 0) rightSpeed += decreasingVelocityDiff;
                else rightSpeed -= decreasingVelocityDiff;
            }

            double min;
            double max;
            if (turning) {
                min = -0.3;
                max = 0.3;
            } else {
                min = -0.6;
                max = 0.6;
            }
            leftMotor.setPower(Range.clip(leftSpeed, min, max));
            rightMotor.setPower(Range.clip(rightSpeed, min, max));

            /**
             * Extra stuff
             */

            if (gamepad2.right_trigger > 0)
                intakeMotor.setPower(-multiplier * gamepad2.right_trigger);
            else if (gamepad2.left_trigger > 0)
                intakeMotor.setPower(multiplier * gamepad2.left_trigger);
            //intakeMotor.setPower(-multiplier * gamepad2.right_trigger + multiplier * gamepad2.left_trigger);

            if (gamepad2.y) targetliftPosition = 1;
            if (gamepad2.b) targetliftPosition = 2;
            if (gamepad2.a) targetliftPosition = 3;
            if (gamepad2.x) targetliftPosition = 4;

            moveToPosition();
        }
    }

    public void moveToPosition() {

    }
}