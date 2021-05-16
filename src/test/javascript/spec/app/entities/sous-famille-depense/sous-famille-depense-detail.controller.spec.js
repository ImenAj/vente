'use strict';

describe('Controller Tests', function() {

    describe('SousFamilleDepense Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSousFamilleDepense, MockFamilleDepense;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSousFamilleDepense = jasmine.createSpy('MockSousFamilleDepense');
            MockFamilleDepense = jasmine.createSpy('MockFamilleDepense');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SousFamilleDepense': MockSousFamilleDepense,
                'FamilleDepense': MockFamilleDepense
            };
            createController = function() {
                $injector.get('$controller')("SousFamilleDepenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:sousFamilleDepenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
